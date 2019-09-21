package xu.leyou.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import xu.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand values(#{param1},#{param2})")
    int addBrandCategory(Long cid, Long bid);

    @Select("SELECT * FROM tb_brand WHERE id IN(SELECT brand_id FROM tb_category_brand WHERE category_id = #{arg0})")
    List<Brand> findByCid(Long cid);
}
