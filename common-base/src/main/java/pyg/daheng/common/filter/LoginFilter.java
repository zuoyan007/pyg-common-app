package pyg.daheng.common.filter;

import org.apache.commons.lang3.StringUtils;
import pyg.daheng.common.config.AppConfig;
import pyg.daheng.common.constants.CommonResponse;
import pyg.daheng.common.constants.RCodeEnums;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.common.constants.SessionKeys;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.utils.HttpServletUtils;
import pyg.daheng.common.utils.RedisUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @Description: 登录过滤器
 * @ClassName: LoginFilter
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:15
 */
public class LoginFilter implements Filter{

    private static final CommonResponse NO_LOGIN = new CommonResponse(RCodeEnums.NO_LOGIN.getCode());

    private static final CommonResponse NO_VIP = new CommonResponse(RCodeEnums.NO_VIP.getCode());

    private static final String TOKEN = "access-token";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        if(Boolean.parseBoolean(AppConfig.getProperty("one.user.login", "false"))){
            String token = request.getHeader(TOKEN);
            if(StringUtils.isNotBlank(token)){
                String status = (String)RedisUtils.get(token);
                if(StringUtils.isNotBlank(status)){
                    CommonResponse appResponse = new CommonResponse(status);
                    switch(status){
                        case ResultCode.DEL_OLD_SESSION:
                            appResponse.setMessage("您的账户已在别处登录，请重新登录并确认账户是否安全");
                            break;
                        case ResultCode.ELE_CUST_NUMBER_NO_WAY:
                            appResponse.setMessage("您的手机号已被其他账户绑定，该账户将被强制下线");
                            break;
                        case ResultCode.ELE_CUST_NUMBER_YES_WAY:
                            appResponse.setMessage("您的手机号已被其他账户绑定，请使用其他方式登录");
                            break;
                        case ResultCode.CANCEL_ACCOUNT_WAY:
                            appResponse.setMessage("感谢您对便宜购的支持，如需再次使用便宜购服务需重新注册账户登录");
                            break;
                    }
                    if(RedisUtils.del(token)){
                        HttpServletUtils.writeJson(appResponse, response);
                        return;
                    }
                }
                //是否允许匿名访问
                if(isAllowAnonymous(request)){
                    chain.doFilter(request,response);
                    return;
                }else {
                    //是否登录
                    if(isLogin(request)){
                        //是否允许非会员访问
                        if(isAllowNonVip(request)){
                            chain.doFilter(request,response);
                            return;
                        }
                        //非会员
                        HttpServletUtils.writeJson(NO_VIP,response);
                    }
                    //未登录
                    HttpServletUtils.writeJson(NO_LOGIN,response);
                }
            }
        }
        UserInfoR user = (UserInfoR)request.getSession().getAttribute(SessionKeys.LOGIN_USER);
        if(user != null){
            response.addHeader("userId", user.getNickName());
        }
        chain.doFilter(request, response);
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
     * 是否允许匿名访问
     * @param request
     * @return
     */
    private boolean isAllowAnonymous(HttpServletRequest request){
        String url = request.getRequestURI();
        String regex = "(/login.*)|(/base.*)";
        return Pattern.matches(regex,url);
    }

    /**
     * 是否允许非会员访问
     * @param request
     * @return
     */
    private boolean isAllowNonVip(HttpServletRequest request){
        String url = request.getRequestURI();
        String regex = "(/login.*)|(/base.*)";
        return Pattern.matches(regex,url);
    }

    @Override
    public void destroy() {

    }
}
