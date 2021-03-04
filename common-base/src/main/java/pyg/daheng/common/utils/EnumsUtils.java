package pyg.daheng.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 处理Enum工具类
 * @author ZhanSSH
 * @date 2021/3/3 15:38
 */
public class EnumsUtils {
    private static Logger logger = LoggerFactory.getLogger(EnumsUtils.class);

    private static Map<Class,Object> map = new ConcurrentHashMap<>();

    /**
     * 根据条件获取枚举对象
     * @param className 枚举类
     * @param predicate 筛选条件
     * @param <T>
     * @return
     */
    public static <T> Optional<T> getEnumObject(Class<T> className, Predicate<T> predicate) {
        if(!className.isEnum()){
            logger.info("Class 不是枚举类");
            return Optional.ofNullable(null);
        }
        Object obj = map.get(className);
        T[] ts = null;
        if(obj == null){
            ts = className.getEnumConstants();
            map.put(className,ts);
        }else{
            ts = (T[])obj;
        }
        return Arrays.stream(ts).filter(predicate).findAny();
    }
}
