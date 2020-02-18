package xu.leyou.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xu.leyou.client.*;
import xu.leyou.item.pojo.Sku;
import xu.leyou.item.pojo.SpecGroup;
import xu.leyou.item.pojo.SpecParam;
import xu.leyou.item.pojo.Spu;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long id) {
        Map<String, Object> map = new HashMap<>();
        Spu spu = spuClient.findBySpuId(id);
        map.put("spu", spu);
        List<Sku> skus = spu.getSkus();
        map.put("skus", skus);
        for (Sku sku : skus) {
            System.out.println(sku);
        }
        List<SpecParam> params = specClient.findParamByCid(spu.getCid3(), null, null);
        Map<Long, String> paramMap = new HashMap<>();
        for (SpecParam param : params) {
            paramMap.put(param.getId(), param.getName());//4 机身颜色，5，内存
        }
        map.put("paramMap", paramMap);
        map.put("categories", categoryClient.findById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())));
        map.put("spuDetail", spu.getSpuDetail());
        System.out.println(spu.getSpuDetail());
        map.put("brand", brandClient.findById(spu.getBrandId()));
        List<SpecGroup> groupByCid = specClient.findGroupByCid(spu.getCid3());
        map.put("specs", groupByCid);
        return map;
    }

    //页面静态化
    public void createHtml(Long id) {
        try {
            //上下文
            Context context = new Context();
            //加载数据
            context.setVariables(loadModel(id));
            //输出流
            File file = new File("K:\\leyou\\upload", id + ".html");
            if (file.exists()) {
                //删除已存在的文件
                file.delete();
            }
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            //输出html文件
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            log.error("静态页生成异常", e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void deletePage(Long spuId) {
        File file = new File("K:\\leyou\\upload", spuId + ".html");
        if (file.exists()) {
            //删除已存在的文件
            file.delete();
        }
    }
}
