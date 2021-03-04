package pyg.daheng.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description:
 * @ClassName: HttpServletUtils
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:33
 */
@Slf4j
public class HttpServletUtils {

    private HttpServletUtils(){}

    /**
     * 写出json 若obj为String 则直接输出
     *
     * @param obj
     * @param response
     */
    public static void writeJson(Object obj, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try(PrintWriter writer = response.getWriter()) {
            if(obj instanceof  String){
                writer.write((String) obj);
            }else {
                writer.write(JSON.toJSONString(obj));
            }
        } catch (IOException e) {
            log.error("#writeJson#", e);
        }
    }

    /**
     * filter 获取对象
     * @param filterConfig
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(FilterConfig filterConfig, Class<T> cls){
        ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        return  ac.getBean(cls);
    }

    /**
     * 重定向
     * @param response
     * @param url
     */
    public static  void sendRedirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);// 之后要修改回正确的常量名
        } catch (IOException e) {
            log.error("#sendRedirect#",e);
        }
    }
}
