package xu.leyou.item.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xu.leyou.item.pojo.Spu;
import xu.leyou.item.pojo.SpuDetail;
import xu.leyou.vo.PageResult;

import java.util.List;

public interface SpuClient {
    @GetMapping("spu/page")
    PageResult<Spu> findByPage(@RequestParam(value = "key", required = false) String key,
                               @RequestParam(value = "saleable", required = false) Boolean saleable,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    @GetMapping("spu/detail/{spuId}")
    SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId);


    @GetMapping("findAllSaleable")
    List<Spu> findAllSaleable();

    @GetMapping("spu/findBySpuId/{id}")
    Spu findBySpuId(@PathVariable("id") Long id);
}
