package xu.leyou.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import xu.leyou.item.pojo.Stock;

public interface StockMapper extends Mapper<Stock> {
    @Delete("DELETE FROM tb_stock  WHERE sku_id IN(SELECT id FROM tb_sku WHERE spu_id = #{arg0})")
    int deleteBySpuId(Long spuId);


    @Update("update tb_stock set stock = stock - #{arg1} where sku_id = #{arg0} and stock >= 1")
    int reduceStock(Long skuId, int num);
}
