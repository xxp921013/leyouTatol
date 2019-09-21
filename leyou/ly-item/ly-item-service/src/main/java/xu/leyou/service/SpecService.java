package xu.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.SpecGroup;
import xu.leyou.item.pojo.SpecParam;
import xu.leyou.mapper.SpecGroupMapper;
import xu.leyou.mapper.SpecParamMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamService specParamService;

    public List<SpecGroup> findGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> groups = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(groups)) {
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FIND);
        }
        for (SpecGroup group : groups) {
            List<SpecParam> paramByGid = specParamService.findParamByGid(group.getId());
            if (paramByGid == null) {
                throw new LyException(ExceptionEnums.FIND_SPEC_PARAM_ERROR);
            }
            group.setParams(paramByGid);
        }
        //list转map,一次查询节省时间
//        List<SpecParam> params = specParamService.findParam(cid, null, null);
//        Map<Long, List<SpecParam>> map = new HashMap<>();
//        for (SpecParam param : params) {
//            if (map.containsKey(param.getGroupId())) {
//                //map的key是组id
//                map.put(param.getGroupId(), new ArrayList<>());
//            }
//            //根据组id(map的key)把param加到对应的list中
//            map.get(param.getGroupId()).add(param);
//        }
//        for (SpecGroup group : groups) {
//            //获取map中的param集合,添加到对应group中
//            group.setParams(map.get(group.getId()));
//        }
        return groups;
    }

    @Transactional
    public void addGroup(SpecGroup specGroup) {
        int insert = specGroupMapper.insert(specGroup);
        if (insert != 1) {
            throw new LyException(ExceptionEnums.ADD_SPEC_GROUP_ERROR);
        }
    }
}
