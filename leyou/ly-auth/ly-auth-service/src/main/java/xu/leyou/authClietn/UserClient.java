package xu.leyou.authClietn;

import org.springframework.cloud.openfeign.FeignClient;
import xu.leyou.userClient.UserApi;

@FeignClient("user-service")
public interface UserClient extends UserApi {
}
