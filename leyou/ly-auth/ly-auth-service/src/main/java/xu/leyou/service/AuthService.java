package xu.leyou.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import xu.leyou.authClietn.UserClient;
import xu.leyou.config.JwtConfig;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.pojo.User;
import xu.leyou.pojo.UserInfo;
import xu.leyou.utils.JwtUtils;

@Service
@Slf4j
@EnableConfigurationProperties(JwtConfig.class)
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtConfig jwtConfig;

    //验证用户名密码是否正确,如果正确则返回生成的token
    public String login(String username, String password) {
        User user = userClient.findUserByUsernameAndPassword(username, password);
        if (user == null) {
            throw new LyException(ExceptionEnums.FIND_USER_ERROR);
        }
        try {
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtConfig.getPrivateKey(), jwtConfig.getExpire());
            return token;
        } catch (Exception e) {
            log.error("token生成失败", e);
            throw new LyException(ExceptionEnums.USER_TOKEN_CREATE_ERROR);
        }

    }
}
