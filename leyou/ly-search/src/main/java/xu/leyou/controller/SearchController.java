package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.pojo.Goods;
import xu.leyou.pojo.SearchRequest;
import xu.leyou.pojo.SearchResult;
import xu.leyou.service.SearchService;
import xu.leyou.service.TestService;
import xu.leyou.vo.PageResult;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private TestService testService;
    @Autowired
    private SearchService searchService;

    @GetMapping("search/load")
    public ResponseEntity<Void> loadData(@RequestParam(value = "id", required = false) Long id) {
        testService.loadData();
        return ResponseEntity.ok().build();
    }

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest sr) {
        System.out.println("getFilter----------" + sr.getFilter());
        SearchResult search = searchService.search(sr);
        return ResponseEntity.ok().body(search);
    }
}
