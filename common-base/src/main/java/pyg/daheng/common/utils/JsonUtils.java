package pyg.daheng.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 处理json串工具类
 */
public class JsonUtils {

    /**
     * 返回对象
     *
     * @param o
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getObject(Object o, Class<T> cls) {

        try {
            return JSON.parseObject(JSON.toJSONString(o), cls);
        } catch (Exception e) {
            return JSON.parseObject(o.toString(), cls);
        }
    }

    /**
     * 返回集合
     *
     * @param o
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(Object o, Class<T> cls) {

        return JSONArray.parseArray(JSON.toJSONString(o), cls);
    }

    /**
     * 对象转json对象
     *
     * @author xuanweiyao
     * @date 12:13 2019/8/31
     */
    public static JSONObject toJsonObject(Object o) {
        try {
            return JSONObject.parseObject(JSONObject.toJSONString(o));
        } catch (Exception e) {
            return JSONObject.parseObject(o.toString());
        }
    }

    /**
     * 对象转json字符串
     * 
     * @author xuanweiyao
     * @date 2019/9/21 16:50
     */
    public static String toJsonString(Object o) {
        return JSONObject.toJSONString(o);
    }
}
