package xu.leyou.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import xu.leyou.order.pojo.Order;
import xu.leyou.order.pojo.OrderDto;
import xu.leyou.order.service.OrderService;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> addOrders(@RequestBody OrderDto orderDto) {
        Long orderId = orderService.addOrders(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @GetMapping({"id"})
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Long id) {
        Order orderById = orderService.findOrderById(id);
        return ResponseEntity.ok(orderById);
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id") Long id) {
        String payUrl = orderService.createPayUrl(id);
        return ResponseEntity.ok(payUrl);

    }

    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> findState(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.findState(id));
    }
}
