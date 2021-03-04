package pyg.daheng.base.util.invoke.util;

import cn.csg.lib.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:38
 */
public class DXMSMonitor implements IMonitorTransation {
    private static MonitorConfig config;
    private static String appName;
    private static QueueMonitor queueMonitor;
    private static DXQueueManager qManager = DXQueueManager.getInstance();
    private static volatile DXMSMonitor instance;
    private static AtomicInteger TRANS_CYCLE;
    private static final String NODEID_PROPERTY_NAME = "dxnodeid";
    private static final String NODEID_SEPERATOR = "-";
    private static final Integer CYCLE_LIMITED = 999999999;
    private static String NODEID_DEFAULT;
    private static String nodeid;

    public DXMSMonitor() {
    }

    public static DXMSMonitor getInstance() {
        if (instance == null) {
            Class var0 = DXMSMonitor.class;
            synchronized(DXMSMonitor.class) {
                if (instance == null) {
                    instance = new DXMSMonitor();
                    Random rand = new Random();
                    TRANS_CYCLE = new AtomicInteger();
                    TRANS_CYCLE.set(rand.nextInt(CYCLE_LIMITED));
                    NODEID_DEFAULT = "C" + rand.nextInt(CYCLE_LIMITED);
                }
            }
        }

        return instance;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    public void init(String _appName, Properties _prop) {
        config = MonitorConfig.getInstance();
        appName = _appName;
        config.addProperty("spring.application.name", appName);
        config.init(_prop);
        nodeid = _prop.getProperty("dxnodeid");
        if (nodeid == null || StringUtils.isBlank(nodeid)) {
            nodeid = NODEID_DEFAULT;
        }

    }

    public void modifyProperties(Properties _prop) {
    }

    public synchronized boolean start() {
        if (queueMonitor == null) {
            queueMonitor = new QueueMonitor();
            queueMonitor.start();
        }

        return true;
    }

    public static synchronized String CreateNewTransID() {
        StringBuilder sb = new StringBuilder(nodeid);
        sb.append("-");
        sb.append(DateUtil.Format2Current());
        sb.append("-");
        DXMSMonitor var2 = instance;
        Integer cycle;
        synchronized(instance) {
            cycle = TRANS_CYCLE.getAndIncrement();
            if (cycle > CYCLE_LIMITED) {
                TRANS_CYCLE.set(2);
                cycle = 1;
            }
        }

        sb.append(DXBase62.dec2DxDigits(cycle + ""));
        return sb.toString();
    }

    public static synchronized String GetNextSpanID() {
        DXTransItem transitem = (DXTransItem)DXThreadLocal.getDxTransItemTlInstance().get();
        if (transitem != null) {
            String stmp = transitem.getDxspanId();
            return stmp + "." + transitem.GetNextCallXH();
        } else {
            return null;
        }
    }
    @Override
    public String getCurrentTransId() {
        DXTransItem transitem = (DXTransItem)DXThreadLocal.getDxTransItemTlInstance().get();
        return transitem == null ? null : transitem.getDxtransId();
    }
    @Override
    public String getCurrentSpanId() {
        DXTransItem transitem = (DXTransItem)DXThreadLocal.getDxTransItemTlInstance().get();
        return transitem == null ? null : transitem.getDxspanId();
    }
    @Override
    public synchronized String getCurrentSqlRId() {
        DXTransItem transitem = (DXTransItem)DXThreadLocal.getDxTransItemTlInstance().get();
        if (transitem != null) {
            String sqlRId = transitem.getNextSqlRId() + "";
            return sqlRId;
        } else {
            return null;
        }
    }
    @Override
    public void pushMessage(IQueueItem item) {
        qManager.add(item);
    }
}
