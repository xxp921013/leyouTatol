package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.pojo.User;
import xu.leyou.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        Boolean checkUser = userService.checkUser(data, type);
        return ResponseEntity.ok().body(checkUser);
    }

    @PostMapping("/code")
    public ResponseEntity<Void> sendMsg(@RequestParam(value = "phone") String phone) {
        userService.sendMsg(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("register")
    //Valid注解.对象必须通过hibernate validator校验才能传过来数据
    public ResponseEntity<Void> register(@Valid User user, @RequestParam(value = "code") String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("query")
    public ResponseEntity<User> findUserByUsernameAndPassword(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        User user = userService.findUserByUsernameAndPassword(username, password);
        return ResponseEntity.ok().body(user);
    }
}
