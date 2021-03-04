package pyg.daheng.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求日志拦截
 * RequestLog
 * @author ZhanSSH
 * @date 2021/1/14 11:33
 */
@Slf4j
@Component
@Aspect
public class RequestLog {

    @Pointcut("execution(public * pyg.daheng.*.controller.*.*(..))")
    public void controllerLog(){
    }

    @Before("controllerLog()")//前置
    public void before(JoinPoint joinPoint){
        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
                .getRequest();
        log.info("请求IP：{}",request.getRemoteAddr());
        log.info("请求路径：{}",request.getRequestURI());
        log.info("请求方法：{}",request.getMethod());
        log.info("请求参数：{}",getRequestParams(joinPoint,request.getMethod()));
    }

    @Around("controllerLog()")//环绕
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        long endTime = System.currentTimeMillis();
        log.info("执行时间：{} ms",endTime - startTime);
        log.info("返回值：{}\n\t",JSON.toJSON(proceed));
        return proceed;
    }
    private String getRequestParams(JoinPoint joinPoint,String method){
        Object[] args = joinPoint.getArgs();
        Stream<?> stream = ArrayUtils.isEmpty(args) ? Stream.empty() : Arrays.stream(args);
        List<Object> logArgs = stream.filter(arg -> (!(arg instanceof HttpServletRequest)
                && !(arg instanceof HttpServletResponse))).collect(Collectors.toList());
        StringBuilder paramBuilder = new StringBuilder();
        for(int i = 0; i < logArgs.size(); i++){
            try{
                String methodType = HttpMethod.GET.name();
                String param = JSON.toJSONString(logArgs.get(i));
                paramBuilder.append(methodType.equals(method) ? (i != logArgs.size() -1 ? (param +"&"):param):param);

            }catch(Exception e){
                log.error("获取请求参数失败");
            }
        }
        return String.valueOf(paramBuilder);
    }

    @AfterThrowing(throwing = "ex",pointcut = "controllerLog()")//异常通知
    public void afterThrow(Throwable ex){
        log.error("发生异常：{}"+ ex.toString());
    }
}
