package xu.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.item.pojo.SpecParam;
import xu.leyou.mapper.SpecParamMapper;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecParam> findParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> params = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnums.FIND_SPEC_PARAM_ERROR);
        }
        return params;
    }

    @Transactional
    public void addParam(SpecParam specParam) {
        int insert = specParamMapper.insert(specParam);
        if (insert != 1) {
            throw new LyException(ExceptionEnums.ADD_SPEC_PARAM_ERROR);
        }

    }

    @Transactional
    public void updateParam(SpecParam specParam) {
        int i = specParamMapper.updateByPrimaryKey(specParam);
        if (i != 1) {
            throw new LyException(ExceptionEnums.ADD_BRAND_ERROR);
        }

    }

    @Transactional
    public void deleteParamByPid(Long pid) {
        int i = specParamMapper.deleteByPrimaryKey(pid);
        if (i != 1) {
            throw new LyException(ExceptionEnums.DELETE_SPEC_PARAM_ERROR);
        }
    }

    public List<SpecParam> findParamByCid(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        List<SpecParam> params = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnums.FIND_SPEC_PARAM_ERROR);
        }
        return params;
    }

    public List<SpecParam> findParam(Long cid, Long gid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        //有值时会作为搜索条件
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setSearching(searching);
        List<SpecParam> params = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(params)) {
            throw new LyException(ExceptionEnums.FIND_SPEC_PARAM_ERROR);
        }
        return params;
    }
}
