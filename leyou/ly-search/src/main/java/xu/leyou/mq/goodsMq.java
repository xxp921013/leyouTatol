package xu.leyou.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xu.leyou.service.SearchService;

@Component
public class goodsMq {

    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "search.item.insert.queue", durable = "true")
            , exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC), key = {"item.insert", "item.update"}))
    public void listenItemAddAndUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        //处理消息
        searchService.addOrUpdateGoods(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "search.item.delete.queue", durable = "true")
            , exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC), key = {"item.delete"}))
    public void listenItemDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        //处理消息
        searchService.deleteGoods(spuId);
    }
}
