package xu.leyou.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

//fdfs  java配置类,标准写法
@Configuration
//导入jar包自带的配置类
@Import(FdfsClientConfig.class)
//解决jmx重复注册bean
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {

}
