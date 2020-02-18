package xu.leyou.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xu.leyou.pojo.Goods;

import java.util.List;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
