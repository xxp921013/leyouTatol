package xu.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xu.leyou.utils.RsaUtils;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtConfig {
    private String pubKeyPath;
    private String cookieName;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
