package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.item.pojo.Brand;
import xu.leyou.service.BrandService;
import xu.leyou.vo.PageResult;

import javax.naming.spi.ResolveResult;
import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> findAllPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc
    ) {
        PageResult<Brand> byPage = brandService.findByPage(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(byPage);
    }

    @PostMapping
    //只返回状态码,使用ResponseEntity<Void>
    public ResponseEntity<Void> addBrand(
            @RequestParam(value = "cids") List<Long> cids, Brand brand
    ) {
        brandService.addBrand(brand, cids);
        //没有返回值写body
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> findByCid(@PathVariable Long cid) {
        List<Brand> brands = brandService.findByCid(cid);
        return ResponseEntity.ok().body(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> findById(@PathVariable("id") Long id) {
        Brand brand = brandService.findByBid(id);
        return ResponseEntity.ok().body(brand);
    }

}
