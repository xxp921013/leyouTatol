package xu.leyou.item.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xu.leyou.item.pojo.SpecGroup;
import xu.leyou.item.pojo.SpecParam;

import java.util.List;

public interface SpecClient {
    @GetMapping("spec/groups/{cid}")
    List<SpecGroup> findGroupByCid(@PathVariable("cid") Long cid);

    @PostMapping("spec/group")
        //RequestBody接收非表单的json请求,springmvc会自动封装成对象
    Void addGroup(@RequestBody SpecGroup specGroup);

//    @GetMapping("params")
//    public ResponseEntity<List<SpecParam>> findParamByGid(@RequestParam(value = "gid") Long gid) {
//        List<SpecParam> paramByGid = specParamService.findParamByGid(gid);
//        return ResponseEntity.ok(paramByGid);
//    }

    @GetMapping("spec/paramsByCid")
    List<SpecParam> findParamByCid(@RequestParam(value = "cid") Long cid);

    //当请求的方式和地址、参数数量相同时,可通过加入可为空的参数(如下的gid)与其他方法进行区别
    //可传入不同类型的参数(cid.gid.searching)但是方法不变(同样的controller和service)
    @GetMapping("spec/params")
    /**
     * cid
     * gid
     * searching
     * 增加输入的参数,但是每个参数都不是必须的,提高方法的复用性
     */
    List<SpecParam> findParamByCid(@RequestParam(value = "cid", required = false) Long cid,
                                   @RequestParam(value = "gid", required = false) Long gid,
                                   @RequestParam(value = "searching", required = false) Boolean searching);

    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam);

    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam);

    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteParam(@PathVariable(value = "pid") Long pid);
}
