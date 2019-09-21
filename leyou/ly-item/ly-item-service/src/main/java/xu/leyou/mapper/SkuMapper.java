package xu.leyou.mapper;

import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;
import xu.leyou.item.pojo.Sku;

public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku>, SelectByIdListMapper<Sku, Long> {
}
