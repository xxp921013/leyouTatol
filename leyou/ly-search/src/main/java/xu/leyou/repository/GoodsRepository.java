package xu.leyou.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import xu.leyou.pojo.Goods;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {


}
