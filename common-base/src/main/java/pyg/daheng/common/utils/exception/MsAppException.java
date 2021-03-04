package pyg.daheng.common.utils.exception;

/**
 * 异常响应处理类
 * @author ZhanSSH
 * @date 2021/2/5 14:07
 */
public class MsAppException extends RuntimeException {

    protected String sta;

    public String getSta(){return sta;}

    public MsAppException(String status, String message) {
        this(status,message,null);
    }

    public MsAppException(String message) {
        this(null,message,null);
    }

    public MsAppException(String message, Throwable cause) {
        this(null,message, cause);
    }

    public MsAppException(Throwable cause) {
        this(null,null,cause);
    }

    public MsAppException(String status,String message, Throwable cause) {
        super(message, cause);
        this.sta = status;
    }

    /**
     *  条件成立抛出异常
     * @param exception 条件
     * @param message 异常信息
     */
    public static void checkCondicition(boolean exception,String message){
        if(exception){
            throw new MsAppException(message);
        }
    }

    /**
     * 条件成立抛出异常
     * @param exception 条件
     * @param code 异常状态码
     * @param message 异常信息
     */
    public static void checkCondicition(boolean exception,String code,String message){
        if(exception){
            throw new MsAppException(code,message);
        }
    }
}
