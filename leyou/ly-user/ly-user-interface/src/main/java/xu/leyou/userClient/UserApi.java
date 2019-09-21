package xu.leyou.userClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xu.leyou.pojo.User;

import javax.validation.Valid;

public interface UserApi {

    @GetMapping("/check/{data}/{type}")
    Boolean checkUser(@PathVariable("data") String data, @PathVariable("type") Integer type);

    @PostMapping("/code")
    Void sendMsg(@RequestParam(value = "phone") String phone);

    @PostMapping("register")
        //Valid注解.对象必须通过hibernate validator校验才能传过来数据
    Void register(@Valid User user, @RequestParam(value = "code") String code);

    @GetMapping("query")
    User findUserByUsernameAndPassword(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);
}
