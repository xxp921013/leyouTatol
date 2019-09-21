package xu.leyou.order.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.order.config.PayConfig;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PayUtil {
    @Autowired
    private WXPay wxPay;
    @Autowired
    private PayConfig payConfig;

    public String createOrder(Long orderId, Long totalPay, String desc) {
        try {
            Map<String, String> data = new HashMap<>();
            //商品描述
            data.put("body", desc);
            //订单号
            data.put("out_trade_no", orderId.toString());
            //金额,分
            data.put("total_fee", totalPay.toString());
            //调用微信支付的终端ip(实际测试可随意写)
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", payConfig.getNotifyUrl());
            //交易类型
            data.put("trade_type", "NATIVE");
            //向微信支付下订单
            Map<String, String> result = wxPay.unifiedOrder(data);
            //打印结果
            for (Map.Entry<String, String> entry : result.entrySet()) {
                String key = entry.getKey();
                System.out.println(key + (key.length() >= 8 ? "\t:" : "\t\t: ") + entry.getValue());
            }
            //判断通信和业务标识
            isSuccess(result);
            System.out.println("---------------------------");
            String url = result.get("code_url");
            return url;
        } catch (Exception e) {
            log.error("微信支付下单失败", e);
            return null;
        }
    }

    public void isSuccess(Map<String, String> result) {
        //判断下单是否成功
        String return_code = result.get("return_code");
        if (WXPayConstants.FAIL.equals(return_code)) {
            //下单失败
            log.error("微信下单失败", result.get("return_msg"));
            throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
        }
        String result_code = result.get("result_code");
        if (WXPayConstants.FAIL.equals(result_code)) {
            //下单失败
            log.error("微信下单失败", result.get("err_code"), result.get("err_code_des"));
            throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
        }
    }

    public void isValidSign(Map<String, String> reqData) {
        //重新生成签名
        try {
            String s1 = WXPayUtil.generateSignature(reqData, this.payConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
            String s2 = WXPayUtil.generateSignature(reqData, this.payConfig.getKey(), WXPayConstants.SignType.MD5);
            String sign = reqData.get("sign");
            if (!StringUtils.equals(s1, sign) && !StringUtils.equals(s2, sign)) {
                throw new LyException(ExceptionEnums.SIGN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnums.SIGN_ERROR);
        }
    }
}
