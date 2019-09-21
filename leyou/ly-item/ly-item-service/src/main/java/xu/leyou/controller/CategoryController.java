package xu.leyou.controller;

import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xu.leyou.item.pojo.Category;
import xu.leyou.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> findCategoryByPid(@RequestParam("pid") Long pid) {
        /**
         * 根据父节点id查询商品分类
         */
        //return  ResponseEntity.status(HttpStatus.OK).body(null);
        //简写方式
        List<Category> categories = categoryService.findByPid(pid);
        //System.out.println(categories);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> findById(@RequestParam(value = "ids") List<Long> ids) {
        List<Category> categories = categoryService.findByIds(ids);
        return ResponseEntity.ok().body(categories);
    }
}
