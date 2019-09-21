package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.item.pojo.Spu;
import xu.leyou.item.pojo.SpuDetail;
import xu.leyou.service.SpuService;
import xu.leyou.vo.PageResult;

import java.util.List;

@RestController
@RequestMapping("spu")
public class SpuController {
    @Autowired
    private SpuService spuService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Spu>> findByPage(@RequestParam(value = "key", required = false) String key,
                                                      @RequestParam(value = "saleable", required = false) Boolean saleable,
                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<Spu> byPage = spuService.findByPage(page, rows, saleable, key);
        return ResponseEntity.ok().body(byPage);
    }

    @GetMapping("detail/{spuId}")
    public ResponseEntity<SpuDetail> findSpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail detail = spuService.findSpuDetailBySpuId(spuId);
        return ResponseEntity.ok().body(detail);
    }


    @GetMapping("findAllSaleable")
    public ResponseEntity<List<Spu>> findAllSaleable() {
        List<Spu> spus = spuService.findAllSaleable();
        return ResponseEntity.ok().body(spus);
    }

    @GetMapping("findBySpuId/{id}")
    public ResponseEntity<Spu> findBySpuId(@PathVariable("id") Long id) {
        Spu spu = spuService.findBySpuId(id);
        return ResponseEntity.ok().body(spu);
    }
}
