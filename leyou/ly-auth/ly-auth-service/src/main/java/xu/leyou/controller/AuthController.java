package xu.leyou.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.Utils.CookieUtils;
import xu.leyou.config.JwtConfig;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.pojo.User;
import xu.leyou.pojo.UserInfo;
import xu.leyou.service.AuthService;
import xu.leyou.utils.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtConfig.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse resp, HttpServletRequest req) {
        String token = authService.login(username, password);
        //写入cookie
        CookieUtils.setCookie(req, resp, jwtConfig.getCookieName(), token, jwtConfig.getExpire() * 60 * 24);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("verify")
    //@CookieValue获取cookie
    public ResponseEntity<UserInfo> userVerify(@CookieValue("LY_TOKEN") String token, HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isBlank(token)) {
            throw new LyException(ExceptionEnums.UN_AUTHORIZED);
        }
        try {
            System.out.println(token);
            //解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //刷新token,重新生成
            String s = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
            //写入cookie
            CookieUtils.setCookie(req, resp, jwtConfig.getCookieName(), s, jwtConfig.getExpire() * 60 * 24);
            return ResponseEntity.ok().body(userInfo);
        } catch (Exception e) {
            throw new LyException(ExceptionEnums.UN_AUTHORIZED);
        }

    }
}
