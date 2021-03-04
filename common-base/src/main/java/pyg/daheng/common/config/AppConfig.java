package pyg.daheng.common.config;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Description: 配置类
 * @ClassName: AppConfig
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:29
 */
@Slf4j
public class AppConfig {

    final static Properties PROPERTIES = new Properties();

    public static void addProperties(Properties properties){
        PROPERTIES.putAll(properties);
    }

    public static void addProperty(String key,String value){
        PROPERTIES.put(key, value);
    }
    public static void removeProperty(String key){
        PROPERTIES.remove(key);
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        try {
            return PROPERTIES.getProperty(key, defaultValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    public static String getPropertyEncoding(String key,String encoding){

        return getPropertyEncoding(key,encoding, null);
    }

    public static String getPropertyEncoding(String key,String encoding,String defaultValue){
        String str = getProperty(key,defaultValue);
        try {
            //进行编码转换，解决问题
            str = new String(str.getBytes("ISO8859-1"), encoding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        return str;
    }
}
