package xu.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

@Data
public class PayConfig implements WXPayConfig {
    private String appId;  //公众账号id
    private String mchId;  //商户号
    private String key;    //签名秘钥
    private int httpConnectTimeoutMs;  //连接超时时间
    private int httpReadTimeoutMs;     //读取超时时间
    private String notifyUrl;          //下单通知回调地址

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }
}
