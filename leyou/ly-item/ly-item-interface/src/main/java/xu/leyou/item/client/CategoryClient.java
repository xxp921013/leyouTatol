package xu.leyou.item.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xu.leyou.item.pojo.Category;

import java.util.List;

public interface CategoryClient {
    @GetMapping("category/list")
    List<Category> findCategoryByPid(@RequestParam("pid") Long pid);

    @GetMapping("category/list/ids")
    List<Category> findById(@RequestParam(value = "ids") List<Long> ids);
}

