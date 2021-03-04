package pyg.daheng.common.invoke;

import pyg.daheng.common.config.AppConfig;

/**
 * 调用组件常量类
 * @author ZhanSSH
 * @date 2021/2/5 16:43
 */
public class InvokeConstants {
    private InvokeConstants() {}

    public static final int LOG_THRESHOLD = Integer.parseInt(AppConfig.getProperty("pyg.log_threshold", "2000"));
    public static final int LOG_SIZE = Integer.parseInt(AppConfig.getProperty("pyg.log_size", "200"));

}
