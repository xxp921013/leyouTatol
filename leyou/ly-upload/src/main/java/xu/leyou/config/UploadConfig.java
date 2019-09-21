package xu.leyou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

//upload配置类
@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadConfig {
    private String baseUrl;
    private List<String> agreeTypes;
}
