package pyg.daheng.base.util.invoke.util;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:09
 */
public class MsinvokeFilter implements Filter {
    public MsinvokeFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String customKey = MsinvokeAppConfig.getProperty("msinvoke.non.filter");
            if (!StringUtils.isEmpty(customKey)) {
                String[] keyArray = customKey.split(",");
                String[] var7 = keyArray;
                int var8 = keyArray.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String key = var7[var9];
                    if (httpRequest.getRequestURI().equals(key)) {
                        chain.doFilter(request, response);
                    }
                }
            }

            String token = httpRequest.getHeader("dxtoken");
            if (!StringUtils.isEmpty(token)) {
                ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).setDxtoken(token);
            }

            String userid = (String)httpRequest.getSession().getAttribute("uid");
            if (StringUtils.isEmpty(userid)) {
                userid = httpRequest.getHeader("uid");
            }

            if (!StringUtils.isEmpty(userid)) {
                ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).setUserId(userid);
            }

            String baseLoginInfo = httpRequest.getHeader("baseLoginInfo");
            if (StringUtils.isEmpty(baseLoginInfo)) {
                baseLoginInfo = (String)httpRequest.getSession().getAttribute("baseLoginInfo");
            }

            if (!StringUtils.isEmpty(baseLoginInfo)) {
                ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).setBaseLoginInfo(baseLoginInfo);
            }

            String channel = httpRequest.getHeader("channel");
            if (StringUtils.isEmpty(channel)) {
                channel = MsinvokeAppConfig.getProperty("msinvoke.dxqd.flag");
            }

            ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).setChannel(channel);
            chain.doFilter(httpRequest, response);
            MsinvokeThreadLocal.getFilterHeader().remove();
        } else {
            throw new ServletException("CamsOncePerRequestFilter just supports HTTP requests");
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
