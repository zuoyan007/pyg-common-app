package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:27
 */
public interface IQueueItem {
    //获取当前类名
    public String getSimpleName();
    //获取主题
    public String getTopic();
    /**获取 dxtransId DXThreadLocal*/
    public String getDxtransId();
    /**获取 dxspanId DXThreadLocal*/
    public String getDxspanId();
}
