package xu.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecClient extends xu.leyou.item.client.SpecClient {
}
