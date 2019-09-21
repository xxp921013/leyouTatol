package xu.leyou.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xu.leyou.order.interceptor.UserInterceptor;


@Configuration
@EnableConfigurationProperties(JwtConfig.class)
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(new UserInterceptor(jwtConfig)).addPathPatterns("/order/**");
    }
}
