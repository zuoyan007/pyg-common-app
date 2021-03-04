package pyg.daheng.common.chain;

import pyg.daheng.common.config.AppConfig;

/**
 * @author ZhanSSH
 * @date 2021/2/25 18:01
 */
public abstract class AbstractReturnHandler {

    protected static final String DESENSITIZE_FAIL_MSG = "data desensitize fail";

    /**
     * 下一个责任链
     */
    protected AbstractReturnHandler nextHandler;

    protected String enableDesensitize;

    public AbstractReturnHandler(AbstractReturnHandler nextHandler){this.nextHandler=nextHandler;}

    public void handlerResult(Object result){
        enableDesensitize = AppConfig.getProperty("common.enable.desensitize","true");
        handler(result);
        if(nextHandler != null){
            this.nextHandler.handlerResult(result);
        }
    }

    /**
     * 返回值处理
     * @param result 返回结果
     */
    protected abstract void handler(Object result);
}
