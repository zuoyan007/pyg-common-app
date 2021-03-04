package pyg.daheng.base.util.invoke.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:39
 */
public class MonitorConfig {
    private static final Logger log = LoggerFactory.getLogger(MonitorConfig.class);
    private static String CONFIG_RESOURCES = "default-monitor-config.properties";
    private static String DXTransItemNewestPath = null;
    private static String DXTransItemBackupPath = null;
    private static long FileMaximum = 20971520L;
    private static String os = null;
    private static boolean isWindows = true;
    public static Map<String, String> configs = new HashMap();
    private static long queueSize = 200L;
    private static long timeInterval = 5000L;
    private static long requestSize = 20L;
    private static boolean isOff = false;
    private static String filterPath = null;
    private static volatile MonitorConfig instance;

    public MonitorConfig() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println((String)configs.get("monitor.filter.path"));
    }

    public static MonitorConfig getInstance() {
        if (instance == null) {
            Class var0 = MonitorConfig.class;
            synchronized(MonitorConfig.class) {
                if (instance == null) {
                    instance = new MonitorConfig();
                }
            }
        }

        return instance;
    }

    public void init(Properties prop) {
        log.info("### MonitorConfig init() ### prop=" + prop);
        Iterator it = prop.stringPropertyNames().iterator();

        while(it.hasNext()) {
            String key = (String)it.next();
            String value = prop.getProperty(key).trim();
            configs.put(key, value);
        }

    }

    public boolean PropertiesModify(Properties prop) {
        return true;
    }

    public void addProperty(String key, String value) {
        configs.put(key, value);
    }

    public static void getProperties() {
        Properties prop = new Properties();

        try {
            InputStream in = MonitorConfig.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCES);
            log.info("### MonitorConfig getProperties() ### in=" + in);
            prop.load(in);
            Iterator it = prop.stringPropertyNames().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                String value = prop.getProperty(key).trim();
                configs.put(key, value);
            }

            log.info("### MonitorConfig getProperties() ### configs=" + configs);
            in.close();
        } catch (IOException var5) {
            log.error("### MonitorConfig getProperties() ###", var5);
        }

    }

    public static String stitchingPath(String path, String springApplication) {
        int index = path.lastIndexOf("/");
        String prefix = path.substring(0, index + 1);
        String suffix = path.substring(index + 1);
        path = prefix + DXMSMonitor.getInstance().getAppName() + "-" + suffix;
        return path;
    }

    public static String getDXTransItemNewestPath() {
        if (isWindows) {
            DXTransItemNewestPath = (String)configs.get("windows.monitor.info.newestPath");
            if (StringUtils.isEmpty(DXTransItemNewestPath)) {
                DXTransItemNewestPath = "monitor/infoNewest.txt";
            }
        } else {
            DXTransItemNewestPath = (String)configs.get("linux.monitor.info.newestPath");
            if (StringUtils.isEmpty(DXTransItemNewestPath)) {
                DXTransItemNewestPath = "monitor/infoNewest.txt";
            }
        }

        if (!StringUtils.isEmpty(DXMSMonitor.getInstance().getAppName())) {
            DXTransItemNewestPath = stitchingPath(DXTransItemNewestPath, DXMSMonitor.getInstance().getAppName());
        }

        return DXTransItemNewestPath;
    }

    public static String getDXTransItemBackupPath() {
        if (isWindows) {
            DXTransItemBackupPath = (String)configs.get("windows.monitor.info.backupPath");
            if (StringUtils.isEmpty(DXTransItemBackupPath)) {
                DXTransItemBackupPath = "monitor/infoBackup.txt";
            }
        } else {
            DXTransItemBackupPath = (String)configs.get("linux.monitor.info.backupPath");
            if (StringUtils.isEmpty(DXTransItemBackupPath)) {
                DXTransItemBackupPath = "monitor/infoBackup.txt";
            }
        }

        if (!StringUtils.isEmpty(DXMSMonitor.getInstance().getAppName())) {
            DXTransItemBackupPath = stitchingPath(DXTransItemBackupPath, DXMSMonitor.getInstance().getAppName());
        }

        return DXTransItemBackupPath;
    }

    public static Long getFileMaximum() {
        String value = (String)configs.get("monitor.file.maximum");
        if (StringUtils.isEmpty(value)) {
            return FileMaximum;
        } else {
            FileMaximum = Long.parseLong(value);
            return FileMaximum;
        }
    }

    public static long getQueueSize() {
        String value = (String)configs.get("monitor.queue.size");
        if (StringUtils.isEmpty(value)) {
            return queueSize;
        } else {
            queueSize = Long.parseLong(value);
            return queueSize;
        }
    }

    public static long getTimeInterval() {
        String value = (String)configs.get("monitor.timeInterval");
        if (StringUtils.isEmpty(value)) {
            return timeInterval;
        } else {
            timeInterval = Long.parseLong(value);
            return timeInterval;
        }
    }

    public static boolean getIsOff() {
        String value = (String)configs.get("monitor.isOff");
        if (StringUtils.isEmpty(value)) {
            return isOff;
        } else {
            isOff = Boolean.parseBoolean(value);
            return isOff;
        }
    }

    public static long getRemoteQuestSize() {
        String value = (String)configs.get("monitor.remote.request.size");
        if (StringUtils.isEmpty(value)) {
            return requestSize;
        } else {
            requestSize = Long.parseLong(value);
            return requestSize;
        }
    }

    static {
        os = System.getProperty("os.name");
        isWindows = os.toLowerCase().indexOf("windows") > -1;
        getProperties();
    }
}
