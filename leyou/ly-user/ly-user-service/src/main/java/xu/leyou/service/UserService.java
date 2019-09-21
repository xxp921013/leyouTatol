package xu.leyou.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.provider.MD5;
import xu.leyou.Utils.NumberUtils;
import xu.leyou.config.SmsConfig;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.mapper.UserMapper;
import xu.leyou.pojo.User;
import xu.leyou.utils.CodecUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(SmsConfig.class)
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    static final String KEY_PREFIX = "user:phoneCode:";

    @Autowired
    private StringRedisTemplate srt;
    @Autowired
    private SmsConfig smsConfig;

    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if (type == 1) {
            user.setUsername(data);
        } else if (type == 2) {
            user.setPhone(data);
        } else {
            throw new LyException(ExceptionEnums.CHECK_USER_TYPE);
        }
        //selectCount查询到的条数
        return userMapper.selectCount(user) == 0;
    }

    public void sendMsg(String phone) {
        //生存key
        String key = KEY_PREFIX + phone;
        //生存验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        amqpTemplate.convertAndSend(smsConfig.getCodeExchange(), smsConfig.getCodeRouteKey(), map);
        //保存验证码
        srt.opsForValue().set(key, code, 2, TimeUnit.MINUTES);
    }

    @Transactional
    public void register(User user, String code) {
        //取出验证码
        String s = srt.opsForValue().get(KEY_PREFIX + user.getPhone());
        //验证code是否输入正确
        if (StringUtils.isBlank(s) || !s.equals(code)) {
            throw new LyException(ExceptionEnums.FIND_SPEC_PARAM_ERROR);
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        //密码加密
        //String md5Hex = DigestUtils.md5Hex(user.getPassword());
        String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(md5Hex);
        user.setSalt(salt);
        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User one = userMapper.selectOne(user);
        if (one == null) {
            throw new LyException(ExceptionEnums.FIND_USER_ERROR);
        }
        //生成用户输入密码的加密格式跟数据库中的密码比对
        String s = CodecUtils.md5Hex(password, one.getSalt());
        System.out.println(s);
        if (!s.equals(one.getPassword())) {
            throw new LyException(ExceptionEnums.FIND_USER_ERROR);
        }
        return one;
    }
}
