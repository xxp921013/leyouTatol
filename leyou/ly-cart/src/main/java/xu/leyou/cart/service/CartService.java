package xu.leyou.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xu.leyou.Utils.JsonUtils;
import xu.leyou.cart.interceptor.UserInterceptor;
import xu.leyou.cart.pojo.Cart;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.pojo.UserInfo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate srt;

    private static final String KEY_PREFIX = "cart:user:id";

    public void addSkuToCart(Cart cart) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //判断当前商品是否存在
        String key = KEY_PREFIX + user.getId();
        //确定外层key(购物车用户)
        BoundHashOperations<String, Object, Object> operations = srt.boundHashOps(key);
        String hashKey = String.valueOf(cart.getSkuId());
        Boolean hasKey = operations.hasKey(hashKey);
        if (hasKey) {
            //存在
            //取出购物车的json对象
            String json = (String) operations.get(hashKey);
            //转成cart对象
            Cart cacheCart = JsonUtils.parse(json, Cart.class);
            //修改数量
            cacheCart.setNum(cacheCart.getNum() + cart.getNum());
            //存入
            String serialize = JsonUtils.serialize(cacheCart);
            operations.put(hashKey, serialize);
        } else {
            //不存在
            operations.put(hashKey, JsonUtils.serialize(cart));
        }

    }

    public List<Cart> findAll(UserInfo user) {
        String key = KEY_PREFIX + user.getId();
        if (!srt.hasKey(key)) {
            throw new LyException(ExceptionEnums.CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operations = srt.boundHashOps(key);
        List<Object> values = operations.values();
        List<Cart> carts = values.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
        return carts;
    }

    public void updateCart(Long id, Integer num) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> operations = srt.boundHashOps(key);
        String s = operations.get(id.toString()).toString();
        Cart cart = JsonUtils.parse(s, Cart.class);
        cart.setNum(num);
        String serialize = JsonUtils.serialize(cart);
        operations.put(id.toString(), serialize);
    }

    public void deleteCart(Long skuId) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
//        BoundHashOperations<String, Object, Object> operations = srt.boundHashOps(key);
//        operations.delete(skuId.toString());
        srt.opsForHash().delete(key, skuId.toString());
    }
}
