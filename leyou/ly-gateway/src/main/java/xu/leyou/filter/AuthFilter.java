package xu.leyou.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.joda.time.chrono.IslamicChronology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import xu.leyou.config.FilterConfig;
import xu.leyou.config.JwtConfig;
import xu.leyou.enums.ExceptionEnums;
import xu.leyou.exception.LyException;
import xu.leyou.pojo.UserInfo;
import xu.leyou.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties({JwtConfig.class, FilterConfig.class})
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private FilterConfig filterConfig;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;//过滤器顺序
    }

    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取请求url路径
        String uri = request.getRequestURI();
        //判断是否放行
        boolean contains = isAllowPath(uri);
        return !contains;//是否过滤
    }

    private boolean isAllowPath(String uri) {
        for (String allowPath : filterConfig.getAllowPaths()) {
            if (uri.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取token
        String token = request.getHeader("LY_TOKEN");
        //解析token
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
        } catch (Exception e) {
            //拦截
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
            context.setResponseBody("权限不足");
            context.getResponse().setContentType("text/html;charset=utf-8");
        }
        //TODO 校验权限
        return null;
    }

}
