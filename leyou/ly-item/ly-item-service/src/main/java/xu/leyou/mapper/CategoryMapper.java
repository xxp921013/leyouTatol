package xu.leyou.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import xu.leyou.item.pojo.Category;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {

}
