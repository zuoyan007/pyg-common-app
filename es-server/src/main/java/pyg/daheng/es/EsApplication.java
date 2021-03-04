package pyg.daheng.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * ES搜索引擎服务 启动类
 * @author ZhanSSH
 * @date 2021/1/14 16:50
 */
@SpringBootApplication
@ComponentScan(basePackages = "pyg.daheng")
public class EsApplication {
    public static void main(String[] args) throws Exception{
        //加载配置文件
        Properties pro = new Properties();
        pro.load(EsApplication.class.getClassLoader().getResourceAsStream("application.properties"));
        String property = pro.getProperty("spring.profiles.active");
        if(StringUtils.hasText(property)){
            pro.load(EsApplication.class.getClassLoader().getResourceAsStream(String.format("application-%s.properties",property)));
        }
        SpringApplication.run(EsApplication.class, args);
    }
}
