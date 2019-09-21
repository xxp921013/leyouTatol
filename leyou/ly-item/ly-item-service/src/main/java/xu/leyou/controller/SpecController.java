package xu.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.item.pojo.SpecGroup;
import xu.leyou.item.pojo.SpecParam;
import xu.leyou.service.SpecParamService;
import xu.leyou.service.SpecService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("spec")
public class SpecController {
    @Autowired
    private SpecService specService;
    @Autowired
    private SpecParamService specParamService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> findGroupByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> groupByCid = specService.findGroupByCid(cid);
        return ResponseEntity.ok(groupByCid);
    }


    @PostMapping("group")
    //RequestBody接收非表单的json请求,springmvc会自动封装成对象
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup specGroup) {
        System.out.println(specGroup);
        specService.addGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping("params")
//    public ResponseEntity<List<SpecParam>> findParamByGid(@RequestParam(value = "gid") Long gid) {
//        List<SpecParam> paramByGid = specParamService.findParamByGid(gid);
//        return ResponseEntity.ok(paramByGid);
//    }

    @GetMapping("paramsByCid")
    public ResponseEntity<List<SpecParam>> findParamByCid(@RequestParam(value = "cid") Long cid) {
        List<SpecParam> param = specParamService.findParamByCid(cid);
        return ResponseEntity.ok(param);
    }

    //当请求的方式和地址、参数数量相同时,可通过加入可为空的参数(如下的gid)与其他方法进行区别
    //可传入不同类型的参数(cid.gid.searching)但是方法不变(同样的controller和service)
    @GetMapping("params")
    /**
     * cid
     * gid
     * searching
     * 增加输入的参数,但是每个参数都不是必须的,提高方法的复用性
     */
    public ResponseEntity<List<SpecParam>> findParamByCid(@RequestParam(value = "cid", required = false) Long cid,
                                                          @RequestParam(value = "gid", required = false) Long gid,
                                                          @RequestParam(value = "searching", required = false) Boolean searching) {
        List<SpecParam> param = specParamService.findParam(cid, gid, searching);
        return ResponseEntity.ok(param);
    }

    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam) {
        specParamService.addParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam) {
        specParamService.updateParam(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteParam(@PathVariable(value = "pid") Long pid) {
        specParamService.deleteParamByPid(pid);
        return ResponseEntity.ok().build();
    }
}
