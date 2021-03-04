package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:39
 */
public interface IMonitorTransation {
    /** 功能描述: 获取链路TransId
     * @return: java.lang.String
     * @Author: zh_liguanglin
     * @Date: 2019/9/19 17:02
     */
    String getCurrentTransId();

    /** 功能描述:获取链路SpanId
     * @return: java.lang.String
     * @Author: zh_liguanglin
     * @Date: 2019/9/19 17:03
     */
    String getCurrentSpanId();

    /**获取当前sqlId，并自增1
     * @return
     */
    String getCurrentSqlRId();

    /**获取当前微服务名
     * @return
     */
    String getAppName();

    /**推送到队列
     * @param item
     */
    void pushMessage(IQueueItem item);
}
