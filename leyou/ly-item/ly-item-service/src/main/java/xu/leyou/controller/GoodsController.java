package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import xu.leyou.dto.CartDto;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.Sku;
import xu.leyou.item.pojo.Spu;
import xu.leyou.item.pojo.SpuDetail;
import xu.leyou.service.GoodsService;
import xu.leyou.service.SpuService;

import java.util.List;

/*
前端传参方式及格式：
Request Payload的请求正文格式是json格式的字符串： 
{ “loginName” : “admin”, “password” : “123456” }
Form Data的请求正文格式是用key=value&key1=value2格式： 
loginName=admin&password=123456
后台处理： 
对于 Request Payload 请求， 必须加 @RequestBody 才能将请求正文解析到对应的 bean 中，且只能通过 request.getReader() 来获取请求正文内容 
对于 Form Data 请求，无需任何注解，springmvc 会自动使用 MessageConverter 将请求参数解析到对应的 bean，且通过 request.getParameter(…) 能获取请求参数，或者通过@RequestParam接收
 */
@RestController
@RequestMapping
public class GoodsController {
    @Autowired
    private SpuService spuService;
    @Autowired
    private GoodsService goodsService;

    @PostMapping("goods")
    //因为接收的是json结构
    public ResponseEntity<Void> addGoods(@RequestBody Spu spu) {
        //System.out.println(spu);
        goodsService.addGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> findSkuByIds(@RequestParam(value = "ids", required = false) List<Long> ids, @RequestParam(value = "id", required = false) Long id) {
        List<Sku> skus = null;
        if (id == null) {
            skus = goodsService.findSkuBySpuIds(ids);
        }
        if (CollectionUtils.isEmpty(ids)) {
            skus = goodsService.findSkuBySpuId(id);
        }
        return ResponseEntity.ok().body(skus);
    }

//    @GetMapping("sku/list")
//    public ResponseEntity<List<Sku>> findSkuBySpuId(@RequestParam(value = "id") Long id) {
//        List<Sku> skus = goodsService.findSkuBySpuId(id);
//        return ResponseEntity.ok().body(skus);
//    }

    @PutMapping("goods")
    //因为接收的是json结构
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        //System.out.println(spu);
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("stock/reduce")
    public ResponseEntity<Void> reduceStock(@RequestBody List<CartDto> carts) {
        goodsService.reduceStock(carts);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
