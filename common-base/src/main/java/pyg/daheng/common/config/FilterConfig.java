package pyg.daheng.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pyg.daheng.common.filter.LoginFilter;

/**
 * @Description:
 * @ClassName: FilterConfig
 * @Author: ZhanSSH
 * @Date: 2021/1/18 15:54
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter(){
        FilterRegistrationBean<LoginFilter> loginFilter = new FilterRegistrationBean<>();
        loginFilter.setFilter(new LoginFilter());
        loginFilter.addUrlPatterns("/*");
        loginFilter.setOrder(250);
        return loginFilter;
    }
}
