package pyg.daheng.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pyg.daheng.common.annotation.LimitAnnotation;
import pyg.daheng.common.constants.enums.LimitType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 限流 AOP
 * @author ZhanSSH
 * @date 2021/3/3 16:56
 */
@Aspect
@Component
@Slf4j
public class LimitAOP {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("execution(public * * (..)) && @annotation(pyg.daheng.common.annotation.LimitAnnotation)")
    public void limitAOP(){}

    @Around("limitAOP()")
    public Object limitHand(ProceedingJoinPoint joinPoint){
        try{
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            LimitAnnotation limitAnnotation = method.getAnnotation(LimitAnnotation.class);
            LimitType type = limitAnnotation.limitType();
            int limitPeriod = limitAnnotation.period();
            int limitCount = limitAnnotation.count();
            String key;
            switch(type){
                case IP:
                    key = getIpAddress();
                    break;
                case USER_NUMBER:
                    key = getIpAddress() + "-" + limitAnnotation.key();
                    break;
                default:
                    key = StringUtils.upperCase(method.getName());
                    break;
            }
            limit(key,limitPeriod,limitCount);
            return joinPoint.proceed();
        }catch(Throwable e){
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }
    private void limit(String key,int limitPeriod,int limitCount){
        Long size = redisTemplate.opsForList().size(key);
        if(size < limitCount){
            redisTemplate.opsForList().leftPush(key,System.currentTimeMillis());
        }else {
            Long start = (Long)redisTemplate.opsForList().index(key, -1);
            if((System.currentTimeMillis() - start) < limitPeriod * 1000){
                throw new RuntimeException("请求过于频繁，已超阈值!");
            }else {
                redisTemplate.opsForList().leftPush(key,System.currentTimeMillis());
                redisTemplate.opsForList().trim(key,-1,-1);
            }
        }
    }
    private static final String UNKNOWN = "unknown";

    private String getIpAddress() {
        HttpServletRequest request =
                ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
