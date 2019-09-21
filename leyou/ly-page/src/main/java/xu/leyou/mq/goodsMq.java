package xu.leyou.mq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xu.leyou.Service.PageService;

@Component
public class goodsMq {

    @Autowired
    private PageService pageService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "search.page.insert.queue", durable = "true")
            , exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC), key = {"item.insert", "item.update"}))
    public void listenItemAddAndUpdate(Long spuId) {
        if (spuId == null) {
            return;
        }
        //处理消息
        pageService.createHtml(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "search.page.delete.queue", durable = "true")
            , exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC), key = {"item.delete"}))
    public void listenItemDelete(Long spuId) {
        if (spuId == null) {
            return;
        }
        //处理消息
        pageService.deletePage(spuId);
    }
}
