package xu.leyou.item.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xu.leyou.dto.CartDto;
import xu.leyou.item.pojo.Sku;
import xu.leyou.item.pojo.Spu;

import java.util.List;

public interface GoodsClient {
    @PostMapping("goods")
        //因为接收的是json结构
    Void addGoods(@RequestBody Spu spu);

    @GetMapping("sku/list")
    List<Sku> findSkuByIds(@RequestParam(value = "ids", required = false) List<Long> ids, @RequestParam(value = "id", required = false) Long id);

    @PutMapping("goods")
        //因为接收的是json结构
    Void updateGoods(@RequestBody Spu spu);

    @PostMapping("stock/reduce")
    Void reduceStock(@RequestBody List<CartDto> carts);
}
