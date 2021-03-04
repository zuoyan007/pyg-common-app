package pyg.daheng.kafka.config;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description:
 * @ClassName: KafkaConfig
 * @Author: ZhanSSH
 * @Date: 2021/1/14 16:50
 */
@Configuration
public class KafkaConfig {

   /* @Value("${spring.kafka.bootstrap-servers:192.168.106.140:9092}")
    private String servers;

    @Bean
    public KafkaAdminClient kafkaAdminClient(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers",servers);
        return (KafkaAdminClient)KafkaAdminClient.create(properties);
    }*/

}
