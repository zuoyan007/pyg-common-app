package pyg.daheng.kafka.factory;

import com.alibaba.fastjson.JSONArray;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.util.CollectionUtils;
import pyg.daheng.kafka.model.NewTopic;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @ClassName: TopicFactory
 * @Author: ZhanSSH
 * @Date: 2021/1/20 16:28
 */
@Configuration
public class TopicFactory implements BeanFactoryAware {

    @Value("${kafka.bootstrap.servers:192.168.106.140:9092}")
    private String bootstrapServers;

    @Value("${newTopic.list.key:[]}")
    private String newTopic;

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void register(){
        List<NewTopic> list = JSONArray.parseArray(newTopic,NewTopic.class);
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(s->{
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NewTopic.class);
                builder.addConstructorArgValue(s.getTopic());
                builder.addConstructorArgValue(s.getNumPartitions());
                //注入bean
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry)this.beanFactory;
                registry.registerBeanDefinition(s.getTopic(),builder.getBeanDefinition());
            });
        }
    }

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> properties = new HashMap<>(1);
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        return new KafkaAdmin(properties);
    }

    @Bean
    public AdminClient adminClient(){
        return KafkaAdminClient.create(kafkaAdmin().getConfig());
    }
}
