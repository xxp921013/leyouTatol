package xu.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpuClient extends xu.leyou.item.client.SpuClient {
}
