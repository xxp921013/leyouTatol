package xu.leyou.order.service;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.UrlBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xu.leyou.Utils.IdWorker;
import xu.leyou.dto.CartDto;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.Sku;
import xu.leyou.order.client.client.GoodsClient;
import xu.leyou.order.enums.OrderStatusEnum;
import xu.leyou.order.interceptor.UserInterceptor;
import xu.leyou.order.mapper.OrderDetailMapper;
import xu.leyou.order.mapper.OrderMapper;
import xu.leyou.order.mapper.OrderStatusMapper;
import xu.leyou.order.pojo.*;
import xu.leyou.order.utils.PayUtil;
import xu.leyou.pojo.UserInfo;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private PayUtil payUtil;

    @Transactional
    public Long addOrders(OrderDto orderDto) {
        Order order = new Order();
        //1新增订单
        //1.1订单编号,基本信息,雪花算法
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDto.getPaymentType());
        //1.2用户信息
        UserInfo user = UserInterceptor.getUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        //1.3收货人地址信息
        order.setReceiver("虎哥");
        order.setReceiverAddress("航头镇航头路18号传智播客 3号楼");
        order.setReceiverCity("上海");
        order.setReceiverDistrict("浦东新区");
        order.setReceiverMobile("15800000000");
        order.setReceiverZip("2100");
        order.setReceiverState("上海");
        //1.4金额相关
        Map<Long, Integer> cartMap = orderDto.getCarts().stream().collect(Collectors.toMap(CartDto::getSkuId, CartDto::getNum));
        Set<Long> skuIds = cartMap.keySet();
        List<Sku> skuByIds = goodsClient.findSkuByIds(new ArrayList<>(skuIds), null);
        long sum = 0;
        //orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Sku skuById : skuByIds) {
            Integer num = cartMap.get(skuById.getId());
            sum += (skuById.getPrice() * num);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImage(StringUtils.substringBefore(skuById.getImages(), ","));
            orderDetail.setNum(num);
            orderDetail.setOrderId(orderId);
            orderDetail.setPrice(skuById.getPrice());
            orderDetail.setTitle(skuById.getTitle());
            orderDetails.add(orderDetail);
        }
        order.setTotalPay(sum);
        order.setActualPay(sum);
        //1.5 order写入数据库
        int i = orderMapper.insertSelective(order);
        if (i != 1) {
            throw new LyException(ExceptionEnums.ORDER_CREATE_ERROR);
        }
        //新增订单详情
        orderDetailMapper.insertList(orderDetails);
        //新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        orderStatusMapper.insertSelective(orderStatus);
        //下单,减库存
        List<CartDto> carts = orderDto.getCarts();
        goodsClient.reduceStock(carts);return orderId;


    }

    public Order findOrderById(Long id) {
        Order order = new Order();
        order.setOrderId(id);
        Order find = orderMapper.selectByPrimaryKey(order);
        if (find == null) {
            throw new LyException(ExceptionEnums.ORDER_FIND_ERROR);
        }
        //订单详情
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(id);
        List<OrderDetail> details = orderDetailMapper.select(orderDetail);
        if (CollectionUtils.isEmpty(details)) {
            throw new LyException(ExceptionEnums.ORDER_DETAIL_FIND_ERROR);
        }
        find.setOrderDetails(details);
        //订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(id);
        order.setStatus(orderStatus.getStatus());
        return find;
    }

    public String createPayUrl(Long id) {
        Order orderById = this.findOrderById(id);
        //判断订单状态
        Integer status = orderById.getStatus();
        if (!OrderStatusEnum.UN_PAY.equals(status)) {
            throw new LyException(ExceptionEnums.ORDER_DETAIL_FIND_ERROR);
        }
        String payUrl = payUtil.createOrder(id, orderById.getTotalPay(), orderById.getOrderDetails().get(0).getTitle());
        if (StringUtils.isBlank(payUrl)) {
            throw new LyException(ExceptionEnums.ORDER_DETAIL_FIND_ERROR);
        }
        return payUrl;
    }

    public void handleNotify(Map<String, String> result) {
        //数据校验
        payUtil.isSuccess(result);
        //签名校验
        payUtil.isValidSign(result);
        //金额校验
        String totalFeeStr = result.get("total_fee");
        String tradeNoStr = result.get("out_trade_no");
        if (StringUtils.isBlank(totalFeeStr) || StringUtils.isBlank(tradeNoStr)) {
            throw new LyException(ExceptionEnums.SIGN_ERROR);
        }
        long totalFee = Long.valueOf(totalFeeStr);
        long tradeNo = Long.valueOf(tradeNoStr);
        Order orderById = this.findOrderById(tradeNo);
        Long totalPay = orderById.getTotalPay();
        if (!(totalFee == totalPay)) {
            throw new LyException(ExceptionEnums.SIGN_ERROR);
        }
        //修改订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(OrderStatusEnum.PAY_NOT_DELIVER.value());
        orderStatus.setOrderId(tradeNo);
        orderStatus.setPaymentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    public Integer findState(Long id) {
        Order orderById = this.findOrderById(id);
        Integer status = orderById.getStatus();
        if (status == null) {
            throw new LyException(ExceptionEnums.ORDER_FIND_ERROR);
        }
        return status;
    }
}
