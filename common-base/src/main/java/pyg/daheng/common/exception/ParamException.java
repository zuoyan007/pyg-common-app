package pyg.daheng.common.exception;

/**
 * @author ZhanSSH
 * @date 2021/2/5 14:43
 */
public class ParamException extends RuntimeException{

    public ParamException() {
        super("参数异常");
    }

    public ParamException(String msg) {
        super(msg);
    }
}
