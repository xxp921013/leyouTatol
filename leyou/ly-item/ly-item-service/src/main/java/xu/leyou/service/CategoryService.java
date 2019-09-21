package xu.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.Category;
import xu.leyou.mapper.CategoryMapper;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        //通用mapper的select 方法需要传一个对象,然后按照对象的非空属性就行select
        List<Category> categories = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FIND);
        }
        return categories;
    }

    public List<Category> findByIds(List<Long> ids) {
        //mapper继承IdListMapper<Category, Long>接口,获得用list查询的方法<T(返回的数据类型),PK(查询条件的数据类型)>
        List<Category> categories = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FIND);
        }
        return categories;
    }
}
