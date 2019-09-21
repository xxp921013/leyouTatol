package xu.leyou.item.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xu.leyou.item.pojo.Brand;
import xu.leyou.vo.PageResult;

import java.util.List;

public interface BrandClient {
    @GetMapping("brand/page")
    PageResult<Brand> findAllPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc);

    @PostMapping
        //只返回状态码,使用ResponseEntity<Void>
    Void addBrand(
            @RequestParam(value = "cids") List<Long> cids, Brand brand);

    @GetMapping("brand/cid/{cid}")
    List<Brand> findByCid(@PathVariable Long cid);

    @GetMapping("brand/{id}")
    Brand findById(@PathVariable("id") Long id);

}
