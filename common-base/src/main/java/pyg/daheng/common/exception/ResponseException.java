package pyg.daheng.common.exception;

import lombok.Data;

/**
 * @Description: 自定义异常
 * @ClassName: ResponseException
 * @Author: ZhanSSH
 * @Date: 2021/1/22 9:52
 */
@Data
public class ResponseException extends RuntimeException{

    private String sta;

    public ResponseException(String status,String message) {this(status,message,null);}

    public ResponseException(String message) {this(null,message,null);}

    public ResponseException(String status,String message, Throwable cause) {
        super(message, cause);
        this.sta = status;
    }
    public ResponseException(String message, Throwable cause){this(null,message,cause);}

    public ResponseException(Throwable cause) {this(null,null,cause);}

    /**
     * 条件成立抛出异常
     * @param expression
     * @param errorMessage
     */
    public static void checkCondition(boolean expression,String errorMessage){
        if(expression){
            throw new ResponseException(errorMessage);
        }
    }
    /**
     * 条件成立抛出异常
     * @param expression
     * @param errorMessage
     */
    public static void checkCondition(boolean expression,String errorCode,String errorMessage){
        if(expression){
            throw new ResponseException(errorCode,errorMessage);
        }
    }
}
