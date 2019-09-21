package xu.leyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xu.leyou.item.pojo.Brand;
import xu.leyou.item.pojo.Category;
import xu.leyou.vo.PageResult;

import java.util.List;
import java.util.Map;


@Data
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;
    private List<Brand> brands;
    private List<Map<String, Object>> specs;//聚合规格参数(key及待选项)


    public SearchResult() {
    }

    public SearchResult(List<Category> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(List<Goods> items, Long total, Long totalPage, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}