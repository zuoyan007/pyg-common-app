package pyg.daheng.common.model.vo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author weizhengjun
 * @date 2019年3月26日上午9:16:54
 */
@Component
@Slf4j
public class RedisUtil {
    private static final Long SUCCESS = 1L;
    @Autowired
    private RedisTemplate redisTemplate;

    public static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        redisUtil = this;
    }

    public static void set(String key, Object value, int second) {
        redisUtil.redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 设置key-value键值对 存在则不set 且不超时
     * @param key
     * @param value
     * @return
     */
    public static boolean setIfAbsent(String key, Object value) { return redisUtil.redisTemplate.opsForValue().setIfAbsent(key, value); }

    /**
     * 设置key-value键值对
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisUtil.set(key, value, 60 * 30);
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisUtil.redisTemplate.opsForValue().get(key);
    }

    public static long ttl(String key) {
        return redisUtil.redisTemplate.getExpire(key);
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return
     */
    public static boolean del(String key) {
        return redisUtil.redisTemplate.delete(key);
    }

    /**
     * 根据keyList批量删除缓存
     *
     * @param keyList
     * @return
     */
    public static Long del(Collection keyList) {
        return redisUtil.redisTemplate.delete(keyList);
    }

    /**
     * 设置SortedSet
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static boolean setSortedSet(String key, Object value, double score) {
        return redisUtil.redisTemplate.opsForZSet().add(key, value, score);
    }

    public static void setHash(String key, String hashKey, Object value) {
        redisUtil.redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public static Object getHase(String key, String hashKey) {
        return redisUtil.redisTemplate.opsForHash().get(key, hashKey);
    }


    /**
     * 获取整个SortedSet
     *
     * @param key
     * @return
     */
    public static Set getSortedSetRange(String key) {
        return redisUtil.redisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * 根据range获取SortedSet
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set getSortedSetRange(String key, Long start, Long end) {
        return redisUtil.redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 查询某个值在SortedSet的排名
     *
     * @param key
     * @param value
     * @return
     */
    public static Long getSortedSetRank(String key, Object value) {
        return redisUtil.redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 根据SortedSet的key批量移除其中的value
     *
     * @param key
     * @param values
     * @return
     */
    public static Long removeSortedSetValues(String key, List values) {
        return redisUtil.redisTemplate.opsForZSet().remove(key, values.toArray());
    }

    /**
     * 根据SortedSet的key与score，移除score包含在min与max区间内的values
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long removeSortedSetValuesByScore(String key, double min, double max) {
        return redisUtil.redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }


    /**
     * @param lockKey
     * @param value
     * @param expireTime 秒
     * @return
     */
    public static boolean getLock(String lockKey, String value, int expireTime) {
        boolean ret = false;
        try {
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";

            RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);

            Object result = redisUtil.redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value, expireTime);

            if (SUCCESS.equals(result)) {
                return true;
            }

        } catch (Exception e) {

        }
        return ret;
    }

    public static boolean releaseLock(String lockKey, String value) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);

        Object result = redisUtil.redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value);
        if (SUCCESS.equals(result)) {
            return true;
        }

        return false;
    }
}
