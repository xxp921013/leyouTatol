package xu.leyou.order.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import xu.leyou.Utils.CookieUtils;

import xu.leyou.order.config.JwtConfig;
import xu.leyou.pojo.UserInfo;
import xu.leyou.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtConfig jwtConfig;

    public UserInterceptor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    //获得当前线程域(域是map结构)
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        //String token = request.getHeader("LY_TOKEN");
        //解析token
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //传递用户
            //request.setAttribute("user", userInfo);
            //向线程内存入数据
            tl.set(userInfo);
            return true;
        } catch (Exception e) {
            log.error("[购物车]解析用户失败", e);
            //拦截
            return false;
        }
    }

    @Override
    //该方法在视图渲染完之后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //使用完后删除数据
        tl.remove();
    }

    public static UserInfo getUser() {
        return tl.get();
    }
}
