package pyg.daheng.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pyg.daheng.common.constants.SessionKeys;
import pyg.daheng.common.model.result.UserInfoR;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 登录拦截器
 * @ClassName: LoginInterceptor
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:45
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        boolean isAssignableFrom = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if(isAssignableFrom){
            HandlerMethod handlerMethod = (HandlerMethod)handler;

        }
        return super.preHandle(request, response, handler);

    }
    /**
     * 是否登录
     * @param request
     * @return
     */
    private boolean isLogin(HttpServletRequest request){
        UserInfoR userInfo = (UserInfoR)request.getSession().getAttribute(SessionKeys.LOGIN_USER);
        return userInfo != null;
    }

    /**
     * 是否为超级会员
     * @param request
     * @return
     */
    private boolean isSupperVip(HttpServletRequest request){
        String supperVip = (String)request.getSession().getAttribute(SessionKeys.SUPPER_VIP);
        return !StringUtils.isEmpty(supperVip);
    }

}
