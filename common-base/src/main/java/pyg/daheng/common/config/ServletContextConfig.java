package pyg.daheng.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pyg.daheng.common.interceptor.LogInterceptor;
import pyg.daheng.common.interceptor.LoginInterceptor;

/**
 * ServletContextConfig
 * @author ZhanSSH
 * @date 2021/1/18 10:26
 */
@Configuration
public class ServletContextConfig implements WebMvcConfigurer{

    @Bean
    public LoginInterceptor loginInterceptor(){return new LoginInterceptor();}

    @Bean
    public LogInterceptor logInterceptor(){return new LogInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/v2/api-docs").order
                (Integer.MIN_VALUE);

        registry.addInterceptor(logInterceptor()).addPathPatterns("/**").order(50);

    }
}
