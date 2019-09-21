package xu.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import xu.leyou.Utils.JsonUtils;
import xu.leyou.client.*;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.*;
import xu.leyou.pojo.Goods;
import xu.leyou.pojo.SearchRequest;
import xu.leyou.pojo.SearchResult;
import xu.leyou.repository.GoodsRepository;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate et;

    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        //查询分类
        List<Category> categories = categoryClient.findById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> cname = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.findById(spu.getBrandId());
        //搜索字段
        String all = spu.getTitle() + StringUtils.join(cname, " ") + brand.getName();
        //查询sku
        List<Sku> skus = goodsClient.findSkuByIds(null, spuId);
        List<Long> priceList = new ArrayList<>();
        //处理skus
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {
            Map map = new HashMap();
            map.put("title", sku.getTitle());
            //每个sku只需要一张图片
            map.put("images", StringUtils.substringBefore(sku.getImages(), ","));
            map.put("id", sku.getId());
            map.put("price", sku.getPrice());
            skuList.add(map);
            priceList.add(sku.getPrice());
        }
        Map<String, Object> specMap = new HashMap<>();
        //查询规格参数
        List<SpecParam> params = specClient.findParamByCid(spu.getCid3(), null, true);
        SpuDetail detail = spuClient.findSpuDetailBySpuId(spuId);
        if (detail == null) {
            throw new LyException(ExceptionEnums.FIND_SKU_ERROR);
        }
        //获取通用规格参数
        String genericSpec = detail.getGenericSpec();
        Map<Long, String> genericMap = JsonUtils.parseMap(genericSpec, Long.class, String.class);
        //获取特有规格参数
        String specialSpec = detail.getSpecialSpec();
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(specialSpec, new TypeReference<Map<Long, List<String>>>() {
        });
        for (SpecParam param : params) {
            String key = param.getName();
            Object value = "";
            if (param.getGeneric()) {
                value = genericMap.get(param.getId());
                //判断value是否是数值类型
                if (param.getNumeric()) {
                    //分段
                    value = chooseSegment(value.toString(), param);
                }
            } else {
                value = specialMap.get(param.getId());
            }
            specMap.put(key, value);
        }
        //查询商品详情
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setId(spuId);
        goods.setCreateTime(spu.getCreateTime());
        //System.out.println(all);
        goods.setAll(all);//搜索字段
        goods.setPrice(priceList);//sku价格集合
        goods.setSkus(JsonUtils.serialize(skuList));//sku集合的json格式
        goods.setSpecs(specMap);// 规格参数
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest sr) {
        Integer page = sr.getPage() - 1;
        Integer size = sr.getSize();
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder();
        //结果过滤
        nsqb.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        //分页
        nsqb.withPageable(PageRequest.of(page, size));
        //过滤
        QueryBuilder basicQuery = bulidBasicQuery(sr);
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all", sr.getKey());
        nsqb.withQuery(basicQuery);
        //聚合分类和品牌
        //聚合分类
        String cateAgg = "categoryAgg";
        String brandAgg = "brandAgg";
        nsqb.addAggregation(AggregationBuilders.terms(cateAgg).field("cid3"));
        nsqb.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));
        //查询
        //分页结果
        //Page<Goods> search = goodsRepository.search(nsqb.build());
        //分页加聚合结果
        AggregatedPage<Goods> search = et.queryForPage(nsqb.build(), Goods.class);
        //解析结果
        System.out.println("search = " + search);
        long total = search.getTotalElements();
        long totalPages = (long) search.getTotalPages();
        List<Goods> goodsList = search.getContent();
        //解析聚合结果
        Aggregations aggregations = search.getAggregations();
        List<Category> categories = parseCateAgg(aggregations.get(cateAgg));
        List<Brand> brands = parseBrandAgg(aggregations.get(brandAgg));
        //规格参数聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            specs = bulidSpecsAgg(categories.get(0).getId(), basicQuery);
        }
        return new SearchResult(total, totalPages, goodsList, categories, brands, specs);
    }

    private QueryBuilder bulidBasicQuery(SearchRequest sr) {
        //创建bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", sr.getKey()));
        //过滤条件
        Map<String, String> filters = sr.getFilter();
        for (Map.Entry<String, String> stringEntry : filters.entrySet()) {
            String key = stringEntry.getKey();
            String value = stringEntry.getValue();
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, value));
        }

        return boolQueryBuilder;
    }

    private List<Map<String, Object>> bulidSpecsAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //查询需要聚合的规格参数
        List<SpecParam> params = specClient.findParamByCid(cid, null, true);
        //聚合
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder();
        nsqb.withQuery(basicQuery);
        for (SpecParam param : params) {
            String name = param.getName();
            nsqb.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }
        //获取结果
        AggregatedPage<Goods> page = et.queryForPage(nsqb.build(), Goods.class);
        //解析
        Aggregations aggregations = page.getAggregations();
        for (SpecParam param : params) {
            String name = param.getName();
            StringTerms terms = aggregations.get(name);
            List<String> options = terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", options);
            specs.add(map);
        }
        return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Brand> brandList = new ArrayList<>();
            List<Long> collect = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            for (Long aLong : collect) {
                Brand brand = brandClient.findById(aLong);
                brandList.add(brand);
            }
            return brandList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Category> parseCateAgg(LongTerms terms) {
        try {
            List<Long> collect = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
            for (Long aLong : collect) {
                System.out.println("cid------" + aLong);
            }
            List<Category> categories = categoryClient.findById(collect);
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addOrUpdateGoods(Long spuId) {
        //查询spu
        Spu spu = spuClient.findBySpuId(spuId);
        //构建goods
        Goods goods = buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);
        //不抓异常,由spring抛出,消息会回滚,防止消息丢失

    }

    public void deleteGoods(Long spuId) {
        goodsRepository.deleteById(spuId);
    }
}
