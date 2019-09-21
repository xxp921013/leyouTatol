package xu.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ly.sms")
@Data
public class SmsConfig {
    private String codeRouteKey;
    private String codeExchange;

}
