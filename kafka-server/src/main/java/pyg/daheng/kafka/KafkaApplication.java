package pyg.daheng.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @Description:
 * @ClassName: KafkaApplication
 * @Author: ZhanSSH
 * @Date: 2021/1/14 16:50
 */
@SpringBootApplication
@ComponentScan(basePackages = "pyg.daheng")
public class KafkaApplication {
    public static void main(String[] args) throws Exception {
        //加载配置文件
        Properties pro = new Properties();
        pro.load(KafkaApplication.class.getClassLoader().getResourceAsStream("application.properties"));
        String property = pro.getProperty("spring.profiles.active");
        if(StringUtils.hasText(property)){
            pro.load(KafkaApplication.class.getClassLoader().getResourceAsStream(String.format("application-%s.properties",property)));
        }
        SpringApplication.run(KafkaApplication.class, args);

    }
}
