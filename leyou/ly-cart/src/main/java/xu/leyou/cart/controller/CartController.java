package xu.leyou.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.cart.config.JwtConfig;
import xu.leyou.cart.interceptor.UserInterceptor;
import xu.leyou.cart.pojo.Cart;
import xu.leyou.cart.service.CartService;
import xu.leyou.pojo.UserInfo;

import java.util.List;

@RestController
@EnableConfigurationProperties({JwtConfig.class})
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping
    public ResponseEntity<Void> addSkuToCart(@RequestBody Cart cart) {
        cartService.addSkuToCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> findCarts() {
        UserInfo user = UserInterceptor.getUser();
        List<Cart> carts = cartService.findAll(user);
        return ResponseEntity.ok().body(carts);
    }

    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestParam(value = "id") Long id, @RequestParam(value = "num") Integer num) {
        cartService.updateCart(id, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping({"skuId"})
    public ResponseEntity<Void> deleteCart(@PathVariable Long skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
