package xu.leyou.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import xu.leyou.pojo.Goods;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:application.yml"})
public class GoodsRepositoryTest {
    @Autowired
    private ElasticsearchTemplate et;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void demo() {
        et.createIndex(Goods.class);
        et.putMapping(Goods.class);
    }

}