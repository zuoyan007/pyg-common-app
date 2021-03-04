package pyg.daheng.kh.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;

import java.util.Properties;


/**
 * @Description:
 * @ClassName: KhDbApplication
 * @Author: ZhanSSH
 * @Date: 2021/1/14 16:50
 */
@SpringBootApplication
@MapperScan(basePackages = "pyg.daheng.kh.db.mapper")
@ComponentScan(basePackages = "pyg.daheng")
public class KhDbApplication {
    public static void main(String[] args) throws Exception {
        //加载配置文件
        Properties pro = new Properties();
        pro.load(KhDbApplication.class.getClassLoader().getResourceAsStream("application.properties"));
        String property = pro.getProperty("spring.profiles.active");
        if(StringUtils.hasText(property)){
            pro.load(KhDbApplication.class.getClassLoader().getResourceAsStream(String.format("application-%s.properties",property)));
        }
        SpringApplication.run(KhDbApplication.class, args);

    }
}
