package xu.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.Brand;
import xu.leyou.mapper.BrandMapper;
import xu.leyou.vo.PageResult;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Brand> findByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤,复杂条件查询,通过Brand.class字节码文件获取要查询的表
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //过滤条件
            //createCriteria创建条件(数据库内的where...like,in等等),因为letter是char只有一个字母所以用orEqualTo
            example.createCriteria().orLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());
        }
        //排序,拼的是order by后的内容
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC " : " ASC ");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        PageInfo pageInfo = new PageInfo(brands);
        System.out.println(pageInfo.getTotal());
        return new PageResult<Brand>(pageInfo.getTotal(), brands);
    }

    @Transactional
    public void addBrand(Brand brand, List<Long> cids) {
        //新增品牌
        System.out.println("id-----" + brand.getId());
        //id是自增的
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1) {
            throw new LyException(ExceptionEnums.ADD_BRAND_ERROR);
        }
        //通用mapper添加成功后会自动将生成的id回写到原对象中
        System.out.println("id-----" + brand.getId());
        //增加中间表数据
        for (Long cid : cids) {
            int i = brandMapper.addBrandCategory(cid, brand.getId());
            if (i != 1) {
                throw new LyException(ExceptionEnums.ADD_BRAND_CATEGORY_ERROR);
            }
        }
    }

    public Brand findByBid(Long bid) {
        Brand brand = brandMapper.selectByPrimaryKey(bid);
        if (brand == null) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        return brand;
    }

    public List<Brand> findByCid(Long cid) {
        List<Brand> brands = brandMapper.findByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FIND);
        }
        return brands;
    }
}

