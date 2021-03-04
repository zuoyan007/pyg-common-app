package pyg.daheng.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.constants.RCodeEnums;
import pyg.daheng.common.exception.ParamException;
import pyg.daheng.common.utils.exception.MsAppException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 自定义异常封装
 * @author ZhanSSH
 * @date 2021/2/5 14:24
 */
@Slf4j
@RestControllerAdvice
@Order(1)
public class DiyExceptionHandler {

    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常
     * @param e 错误对象
     * @return msappResponse
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public MsAppResponse bindExceptionHandler(BindException e){
        String message = e.getBindingResult().getAllErrors().stream()
                          .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return new MsAppResponse(RCodeEnums.PARAMS_ERROR.getCode(),message);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     * @param e 错误对象
     * @return msappResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public MsAppResponse constraintViolation(ConstraintViolationException e){
        String message = e.getConstraintViolations().stream()
                          .map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return new MsAppResponse(RCodeEnums.PARAMS_ERROR.getCode(),message);
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     * @param e 错误对象
     * @return msappResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public MsAppResponse methodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getAllErrors().stream()
                          .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return new MsAppResponse(RCodeEnums.PARAMS_ERROR.getCode(),message);
    }

    /**
     * 参数异常拦截
     * @param e 错误对象
     * @return msappResponse
     */
    @ExceptionHandler(ParamException.class)
    @ResponseBody
    public MsAppResponse paramException(ParamException e){
        String message = e.getMessage();
        return new MsAppResponse(RCodeEnums.PARAMS_ERROR.getCode(),message);
    }

    /**
     * 自定义异常拦截
     * @param e 错误对象
     * @return msappResponse
     */
    @ExceptionHandler(MsAppException.class)
    @ResponseBody
    public MsAppResponse paramException(MsAppException e){
        return new MsAppResponse(e.getSta(),e.getMessage());
    }

}
