package pyg.daheng.base.util.invoke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:42
 */
public abstract class BaseThread extends Thread{
    // 系统日志对象
    protected Logger logger;
    // 线程控制退出方法
    protected boolean bexit = false;
    // 线程结束就绪标志
    protected boolean readyexit = false;
    // 控制线程连续循环N次后，强制sleep一次，将CPU资源释放给其他线程使用
    protected int maxcontinuetimes = 10;
    // 线程名称
    protected String thread_name;
    public void setThreadName(String v_thread_name) {
        this.thread_name = v_thread_name;
    }
    protected String getThreadName(){
        return this.thread_name;
    }
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // just wake up early
            Thread.currentThread().interrupt();
        }
    }
    // 为了每隔一段时间检查健康程度，设置以下参数用于检查线程运行的安全程度，线程需要执行健康检查工作时，重载checkup()方法即可。
    private final long mgr_check_timeout = 10000;
    public void setExit(){
        bexit = true;
    }
    public void notifyExit(){
        this.setExit();
    }
    // 线程空闲等待时长
    protected long idle_sleep;
    // 线程初始化方法
    public BaseThread(){
        this(null, "DXThread", 100);
    }
    public BaseThread(String v_name){
        this(null,v_name, 100);
    }
    public BaseThread(Logger _log) {
        this(_log, "DXThread", 100);
    }
    public BaseThread(Logger _log, String v_name) {
        this(_log, v_name, 100);
    }
    public BaseThread(Logger _log,String v_name, long v_idle_sleep){
        this.thread_name = v_name;
        idle_sleep = v_idle_sleep < 10 ? 10 : v_idle_sleep;
        idle_sleep = idle_sleep > 1000 ? 1000 : idle_sleep;
        if(_log == null)
            this.logger = LoggerFactory.getLogger(this.getClass());
        else
            this.logger = _log;
    }

    public abstract void init();
    @Override
    public void run(){
        this.logger.info(this.thread_name == null ? "CamsThread" : this.thread_name + "-" + this.getId() + " is running.....");
        boolean bckeckup;
        boolean bdowork;
        int continuetimes = 0;
        long mgr_check_point = System.currentTimeMillis();
        while(true){
            bdowork = false;
            if(bexit){
                break;
            }
            try{
                // do something
                if(checkup_enabled){
                    //					// bcheckup只用于控制是否需要继续执行下次检查，因此无需每次都设置该值，如果有任务需要继续检查，也需要等待主循环的sleep后继续执行
                    if(System.currentTimeMillis() - mgr_check_point > mgr_check_timeout){
                        bckeckup = checkup();	// 没有任务需要继续检查返回false，否则返回true
                        if(!bckeckup){
                            mgr_check_point = System.currentTimeMillis();
                        }
                        else{  // 还有任务需要继续检查，则时间值加1秒，直到不需要再检查
                            mgr_check_point += 1000;
                        }
                    }
                }
                bdowork = dowork();
                continuetimes++;
                if(continuetimes > maxcontinuetimes){	// 长时间运行，强制线程休息
                    bdowork = false;
                }
                if(!bdowork){
                    continuetimes = 0;
                    BaseThread.sleep(idle_sleep);
                }
            }catch(Exception e){
                this.logger.warn(thread_name + " Exception:" + e, e);
            }
        }
        this.readyexit = true;
        this.logger.info(this.thread_name + " begin exit.....");
    }
    // 健康检查方法控制调度
    protected boolean checkup_enabled = false;
    protected void enableCheckup(){ checkup_enabled = true; }
    // 线程对象健康检查方法
    protected boolean checkup(){ return false; }
    /**线程对象的工作任务
     * @return 返回true，线程不等待，继续执行；返回false，线程等待idle_sleep时间后，继续调用该过程
     */
    protected abstract boolean dowork();
    // 等待线程退出
    public void joinThread(){
        try{
            this.join();
        }catch(Exception e){
            this.logger.warn(this.thread_name + " join Exception:" + e);
        }
    }
    // 检查是否结束就绪
    public boolean isReadyExit(){
        return this.readyexit;
    }
    public void waitExit() {
        this.joinThread();
    }
}
