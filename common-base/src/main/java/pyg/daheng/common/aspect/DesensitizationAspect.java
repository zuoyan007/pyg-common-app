package pyg.daheng.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pyg.daheng.common.chain.AbstractReturnHandler;

/**
 * 对controller返回数据MsAppResponse进行脱敏
 * description = @SensitiveInfo(disable = true) 不脱敏
 * message = "SensitiveDisable" 不脱敏
 * @author ZhanSSH
 * @date 2021/3/3 15:28
 */
@Aspect
@Component
public class DesensitizationAspect {
    @Autowired
    private AbstractReturnHandler getChainOfReturnHandler;

    @Pointcut("execution(public * pyg.daheng.*.controller.*.*(..))")
    public void controllerAspect() {
    }

    @AfterReturning(value = "controllerAspect()", returning = "result")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {
        getChainOfReturnHandler.handlerResult(result);
    }
}
