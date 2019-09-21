package xu.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface CategoryClient extends xu.leyou.item.client.CategoryClient {

}
