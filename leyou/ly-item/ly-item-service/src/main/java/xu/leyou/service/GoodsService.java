package xu.leyou.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import xu.leyou.dto.CartDto;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.Sku;
import xu.leyou.item.pojo.Spu;
import xu.leyou.item.pojo.SpuDetail;
import xu.leyou.item.pojo.Stock;
import xu.leyou.mapper.SkuMapper;
import xu.leyou.mapper.SpuDetailMapper;
import xu.leyou.mapper.SpuMapper;
import xu.leyou.mapper.StockMapper;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional
    public void addGoods(Spu spu) {
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spu.setSaleable(true);
        spu.setValid(true);
        int z = spuMapper.insert(spu);
        if (z != 1) {
            log.error("");
            throw new LyException(ExceptionEnums.ADD_SPU_ERROR);

        }
        System.out.println(spu.getId());
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(spu.getCreateTime());
            sku.setLastUpdateTime(new Date());
            int i = skuMapper.insert(sku);
            System.out.println("skuid---------" + sku.getId());
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            int y = stockMapper.insert(stock);
            if (i != 1 || y != 1) {
                throw new LyException(ExceptionEnums.ADD_SKU_ERROR);
            }
        }
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        int insert = spuDetailMapper.insert(spuDetail);
        if (insert != 1) {
            throw new LyException(ExceptionEnums.ADD_SPU_DETAIL_ERROR);
        }
        //发送消息
        amqpTemplate.convertAndSend("item.insert", spu.getId());
    }

    public List<Sku> findSkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
        }
        for (Sku sku2 : skus) {
            Stock stock = stockMapper.selectByPrimaryKey(sku2.getId());
            if (stock == null) {
                throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
            }
            sku2.setStock(stock.getStock());
        }
        return skus;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        spu.setLastUpdateTime(new Date());
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setCreateTime(null);
        //updateByPrimaryKeySelective会对字段进行判断再更新(如果为Null就忽略更新)，如果你只想更新某一字段，可以用这个方法。
        int i = spuMapper.updateByPrimaryKeySelective(spu);
        if (i != 1) {
            throw new LyException(ExceptionEnums.ADD_BRAND_ERROR);
        }
        spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        Example example = new Example(Sku.class);
        Example example2 = new Example(Stock.class);
        example.createCriteria().andEqualTo("spuId", spu.getId());
        skuMapper.deleteByExample(example);
        stockMapper.deleteBySpuId(spu.getId());
//        for (Sku sku : spu.getSkus()) {
//            sku.setSpuId(spu.getId());
//            sku.setLastUpdateTime(new Date());
//            int z = skuMapper.insert(sku);
//            Stock stock = new Stock();
//            stock.setSkuId(sku.getId());
//            stock.setStock(sku.getStock());
//            int y = stockMapper.insert(stock);
//            if (i != 1 || y != 1) {
//                throw new LyException(ExceptionEnums.ADD_SKU_ERROR);
//            }
//        }
        saveSkuAndStock(spu);
        //发送消息
        amqpTemplate.convertAndSend("item.update", spu.getId());
    }

    public void saveSkuAndStock(Spu spu) {
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            int i = skuMapper.insert(sku);
            System.out.println("skuid---------" + sku.getId());
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            int y = stockMapper.insert(stock);
            if (i != 1 || y != 1) {
                throw new LyException(ExceptionEnums.ADD_SKU_ERROR);
            }
        }
    }

    public List<Sku> findSkuBySpuIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
        }
        for (Sku sku2 : skus) {
            Stock stock = stockMapper.selectByPrimaryKey(sku2.getId());
            if (stock == null) {
                throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
            }
            sku2.setStock(stock.getStock());
        }
        return skus;
    }

    public void reduceStock(List<CartDto> carts) {
        for (CartDto cart : carts) {
            int i = stockMapper.reduceStock(cart.getSkuId(), cart.getNum());
            if (i != 1) {
                throw new LyException(ExceptionEnums.STOCK_IS_EMPTY);
            }
        }
    }
}
