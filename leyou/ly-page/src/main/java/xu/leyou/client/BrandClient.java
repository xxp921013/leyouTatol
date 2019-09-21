package xu.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends xu.leyou.item.client.BrandClient {
}
