package xu.leyou.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import xu.leyou.ItemServiceApplication;
import xu.leyou.item.pojo.Category;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemServiceApplication.class)
@ContextConfiguration(locations = {"classpath:application.yml"})
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void demo1() {
        List<Category> categories = categoryService.findByPid(0L);
        System.out.println(categories);
    }
}