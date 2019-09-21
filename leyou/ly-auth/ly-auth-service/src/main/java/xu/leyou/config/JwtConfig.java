package xu.leyou.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xu.leyou.utils.RsaUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;
    private String cookieName;
    private PrivateKey privateKey;
    private PublicKey publicKey;


    @PostConstruct
    //初始化公私钥,在构造方法执行后执行
    public void init() throws Exception {
        File pubKey = new File(pubKeyPath);
        File priKey = new File(priKeyPath);
        if (!pubKey.exists() || !priKey.exists()) {
            //生成公私钥
            RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
        }
        //获取公私钥
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}

