package xu.leyou.mq;

import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xu.leyou.Utils.JsonUtils;
import xu.leyou.config.SmsConfig;
import xu.leyou.untils.SmsUtils;

import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(SmsConfig.class)
public class SmsMq {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsConfig smsConfig;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "ly.sms.verify.queue", durable = "true"), exchange = @Exchange(name = "ly.sms.exchange", type = ExchangeTypes.TOPIC), key = "sms.verify.code"))
    public void listenSms(Map<String, String> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        try {
            smsUtils.sendSms(phone, JsonUtils.serialize(msg), smsConfig.getSign(), smsConfig.getVerifyCodeTemplate());
        } catch (ClientException e) {
            log.error("短信发送异常", e);
        }
    }
}
