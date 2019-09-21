import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import xu.leyou.SearchApplication;
import xu.leyou.client.CategoryClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
@ContextConfiguration(locations = {"classpath:application.yml"})
public class CategoryServiceTest {
    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void demo1() {

    }
}