package pyg.daheng.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.controller.BaseController;
import pyg.daheng.common.utils.JsonUtils;
import pyg.daheng.common.utils.SensitiveUtil;

/**
 * 结果拦截处理
 * ResultHander
 * @author ZhanSSH
 * @date 2021/1/14 11:11
 */
@Aspect
@Component
@Slf4j
public class ResultHandler {

    @Pointcut("execution(public * pyg.daheng.*.controller.*.*(..))")
    public void controllerResult(){}

    @Around("controllerResult()")
    public Object beforeReturn(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        R r = (R) proceed;
        log.info("接口请求结果==>>{}", JsonUtils.toJsonString(r));
        return r;
    }

}
