package xu.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.*;
import xu.leyou.mapper.SkuMapper;
import xu.leyou.mapper.SpuDetailMapper;
import xu.leyou.mapper.SpuMapper;
import xu.leyou.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpuService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsService goodsService;

    public PageResult<Spu> findByPage(Integer page, Integer rows, Boolean saleable, String key) {
        List<Spu> spus = null;
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        if (!StringUtils.isBlank(key) && saleable != null) {
            example.createCriteria().andLike("title", "%" + key + "%").andEqualTo("saleable", saleable);
            spus = spuMapper.selectByExample(example);
        } else if (StringUtils.isBlank(key) && saleable != null) {
            example.createCriteria().andEqualTo("saleable", saleable);
            spus = spuMapper.selectByExample(example);
        } else if (!StringUtils.isBlank(key) && saleable == null) {
            example.createCriteria().andLike("title", "%" + key + "%");
            spus = spuMapper.selectByExample(example);
        } else {
            spus = spuMapper.selectAll();
        }
        //查询

        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnums.CAN_NOT_FIND_SPU);
        }

        //解析分类和品牌
        loadCategoryAndBrand(spus);
        PageInfo pageInfo = new PageInfo(spus);
        return new PageResult<Spu>(pageInfo.getTotal(), spus);
    }

    private void loadCategoryAndBrand(List<Spu> spus) {
        for (Spu spu : spus) {
            //处理分类名称
            List<String> names = categoryService.findByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            //处理品牌名称
            Brand brand = brandService.findByBid(spu.getBrandId());
            spu.setBname(brand.getName());
        }
    }

    public SpuDetail findSpuDetailBySpuId(Long spuId) {
//        SpuDetail spuDetail = new SpuDetail();
//        spuDetail.setSpuId(spuId);
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        return spuDetail;
    }

    public List<Spu> findAllSaleable() {
        Spu spu = new Spu();
        spu.setSaleable(true);
        List<Spu> spuList = spuMapper.select(spu);
        return spuList;
    }

    public Spu findBySpuId(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new LyException(ExceptionEnums.CAN_NOT_FIND_SPU);
        }
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null) {
            throw new LyException(ExceptionEnums.CAN_NOT_FIND_SPU);
        }
        spu.setSpuDetail(spuDetail);
        List<Sku> skus = goodsService.findSkuBySpuId(id);
        spu.setSkus(skus);
        return spu;
    }
}
