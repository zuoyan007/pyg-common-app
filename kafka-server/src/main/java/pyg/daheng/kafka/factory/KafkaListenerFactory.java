package pyg.daheng.kafka.factory;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pyg.daheng.kafka.annotation.KafkaListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @Description:
 * @ClassName: KafkaListenerFactory
 * @Author: ZhanSSH
 * @Date: 2021/1/20 17:45
 */
@Component
@Slf4j
public class KafkaListenerFactory implements BeanFactoryAware{

    @Autowired
    private BeanFactory beanFactory;

    @Value("${kafka.package.name:pyg.daheng.kafka}")
    private String pkgName;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    @PostConstruct
    public void register(){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
        .forPackages(pkgName)//指定打包路径
        .addScanners(new SubTypesScanner())//添加子类扫描工具
        .addScanners(new FieldAnnotationsScanner())//添加属性注解扫描
        .addScanners(new MethodParameterNamesScanner())//添加方法参数扫描
        .addScanners(new MethodAnnotationsScanner())//添加方法注解扫描
        );
        // 获取带有特定注解对应的方法
        Set<Method> methods = reflections.getMethodsAnnotatedWith(KafkaListener.class);
        if(ObjectUtils.isEmpty(methods)){
            log.warn("Not exists any method with the KafkaListener annotation");
            return;
        }
        //循环注入kafka的ConcurrentMessageListenerContainer的bean
    }
}
