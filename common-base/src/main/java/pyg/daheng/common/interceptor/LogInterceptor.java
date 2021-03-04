package pyg.daheng.common.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Description:
 * @ClassName: LogInterceptor
 * @Author: ZhanSSH
 * @Date: 2021/1/19 10:20
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter{

    private final static String SESSION_ID = "sessionId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        //设置sessionId
        String token = UUID.randomUUID().toString().replaceAll("-","");
        MDC.put(SESSION_ID,token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {
        //删除sessionId
        MDC.remove(SESSION_ID);
        super.afterCompletion(request, response, handler, ex);
    }
}
