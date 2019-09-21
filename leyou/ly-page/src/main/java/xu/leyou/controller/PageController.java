package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xu.leyou.Service.PageService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long id, Model model) {
        //查询模型数据
        Map<String, Object> attributes = null;
        attributes = pageService.loadModel(id);
        //准备模型数据
        model.addAllAttributes(attributes);
        return "item";
    }
}
