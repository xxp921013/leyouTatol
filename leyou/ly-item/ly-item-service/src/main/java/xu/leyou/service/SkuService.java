package xu.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xu.leyou.mapper.SkuMapper;

@Service
public class SkuService {
    @Autowired
    private SkuMapper skuMapper;
}
