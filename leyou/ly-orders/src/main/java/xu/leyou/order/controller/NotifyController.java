package xu.leyou.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xu.leyou.order.service.OrderService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("wxpay")
public class NotifyController {
    @Autowired
    private OrderService orderService;

    //produces声明返回值的类型
    @GetMapping(value = "notify", produces = "application/xml")
    public Map<String, String> payResult(@RequestBody Map<String, String> result) {
        //处理回调
        orderService.handleNotify(result);
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        return map;
    }

}
