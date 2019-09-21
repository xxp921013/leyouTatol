package xu.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsConfig {
    String accessKeyId;
    String accessKeySecret;
    String sign; //签名
    String verifyCodeTemplate;//模板
}
