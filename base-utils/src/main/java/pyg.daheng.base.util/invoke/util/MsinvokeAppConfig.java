package pyg.daheng.base.util.invoke.util;

import java.util.Properties;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:25
 */
public class MsinvokeAppConfig {
    private static Properties properties = new Properties();

    public MsinvokeAppConfig() {
    }

    public static void addProperties(Properties properties) {
        properties.putAll(properties);
    }

    public static void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public static void removeProperty(String key) {
        properties.remove(key);
    }

    public static String getProperty(String key) {
        return getProperty(key, (String)null);
    }

    public static String getProperty(String key, String defaultValue) {
        try {
            return properties.getProperty(key, defaultValue);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static void init(Properties properties) {
        properties.putAll(properties);
    }

    public static void refresh(Properties properties) {
        init(properties);
    }
}
