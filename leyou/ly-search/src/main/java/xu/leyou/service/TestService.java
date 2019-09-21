package xu.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;
import xu.leyou.client.GoodsClient;
import xu.leyou.client.SpuClient;
import xu.leyou.item.pojo.Spu;
import xu.leyou.pojo.Goods;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SearchService searchService;
    @Autowired
    private ElasticsearchRepository et;

    public void loadData() {
        List<Spu> items = spuClient.findAllSaleable();
        List<Goods> goodsList = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
//            for (Spu item : items) {
//                Goods goods = searchService.buildGoods(item);
//                goodsList.add(goods);
//            }
        et.saveAll(goodsList);
    }
}
