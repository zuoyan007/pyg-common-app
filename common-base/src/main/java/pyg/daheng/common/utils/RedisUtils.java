package pyg.daheng.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import pyg.daheng.common.constants.CacheKey;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @Description:
 * @ClassName: RedisUtils
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:00
 */
public class RedisUtils {
    private static final Long SUCCESS = 1L;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate strRedisTemplate;

    public static RedisUtils redisUtil;

    @PostConstruct
    public void init() {
        redisUtil = this;
    }

    public static void set(String key, Object value, long second, TimeUnit timeUnit) {
        redisUtil.redisTemplate.opsForValue().set(key, value, second, timeUnit);
    }

    public static void set(String key, Object value, int second) {
        redisUtil.redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 设置key-value键值对 存在则不set 且不超时
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setIfAbsent(String key, Object value) {
        return redisUtil.redisTemplate.opsForValue().setIfAbsent(key, value);
    }


    /**
     * 设置key-value键值对 ,不超时
     *
     * @param key
     * @param value
     * @return
     */
    public static void setNotExpired(String key, Object value) {
        redisUtil.redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 设置key-value键值对
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisUtil.redisTemplate.opsForValue().set(key, value, CacheKey.SECOND_PER_MINUTE * 30, TimeUnit.SECONDS);
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

    public static void increment(String key, int value) {
        redisUtil.redisTemplate.opsForValue().increment(key, value);
    }

    public static Object getByBytes(String key) {
        return redisUtil.redisTemplate.opsForValue().get(key.getBytes());
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

    public static Object getHash(String key, String hashKey) {
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

    public static boolean getLock(String lockKey, String value, long expireTime) {
        String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
        List<String> args = Arrays.asList(value, String.valueOf(expireTime));
        List<String> keys = Arrays.asList(lockKey);
        Object execute = eval(script, keys, args);
        return SUCCESS.equals(execute);
    }


    public static boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        List<String> keys = Arrays.asList(lockKey);
        List<String> args = Arrays.asList(value);
        Object execute = eval(script, keys, args);
        return SUCCESS.equals(execute);

    }


    /**
     * 上锁 将键值对设定一个指定的时间timeout.
     *
     * @param key
     * @param timeout 键值对缓存的时间，单位是秒
     * @return 设置成功返回true，否则返回false
     */
    public static boolean tryLock(String key, String value, long timeout) {
        // 底层原理就是Redis的setnx方法
        boolean isSuccess = redisUtil.redisTemplate.opsForValue().setIfAbsent(key, value);
        if (isSuccess) {
            // 设置分布式锁的过期时间
            redisUtil.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return isSuccess;
    }
    /**
     * 保存list
     *
     * @param key
     * @param value
     * @return
     */
    public static Long saveList(String key, Object value) {
        Long workDays = redisUtil.redisTemplate.opsForList().rightPushAll(key, value);
        return workDays;
    }

    public static Long saveList(String key, List value) {
        Long workDays = redisUtil.redisTemplate.opsForList().rightPushAll(key, value);
        return workDays;
    }

    /**
     * 判断是否存在某个key
     *
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        return redisUtil.redisTemplate.hasKey(key);
    }

    /**
     * 获取list中的某个value
     *
     * @param key
     * @param index
     * @return
     */
    public static Object getValueInList(String key, Long index) {
        return redisUtil.redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取list的长度
     *
     * @param key
     * @return
     */
    public static Object getListSize(String key) {
        return redisUtil.redisTemplate.opsForList().size(key);
    }

    /**
     * 删除list中的指定值
     *
     * @param key
     * @return
     */
    public static Object delValueInList(String key, String value) {
        return redisUtil.redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 取出list
     *
     * @param key
     * @return
     */
    public static Object getList(String key) {
        return redisUtil.redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 保存数据到set
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public static Object saveToSet(String key, String value, Double score) {
        return redisUtil.redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 从Set获取变量指定区间的数据
     *
     * @param key
     * @return
     */
    public static Set getSet(String key, long start, long end) {
        return redisUtil.redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 删除Set中的数据
     *
     * @param key
     * @return
     */
    public static Long delValueInSet(String key, long start, long end) {
        return redisUtil.redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 从Set获取变量指定区间的数据
     *
     * @param key
     * @return
     */
    public static Set getSetWithScores(String key, long start, long end) {
        return redisUtil.redisTemplate.opsForZSet().rangeByScoreWithScores(key, start, end);
    }

    public static Long incr(String key, String value, long expireTime, String maxValue) {
        String script = "if redis.call('get',KEYS[1]) then if tonumber(redis.call('get',KEYS[1])) >= tonumber(ARGV[3]) then return tonumber(ARGV[3]) else if redis.call('incrBy',KEYS[1],ARGV[1]) then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end else if redis.call('incrBy',KEYS[1],ARGV[1]) then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end;";
        List<String> keys = Arrays.asList(key);
        List<String> args = Arrays.asList(value, String.valueOf(expireTime), maxValue);
        Object execute = eval(script, keys, args);
        return (Long) execute;

    }

    /**
     * 计数器
     */
    public static long incr(String key, long i) {
        return redisUtil.redisTemplate.getConnectionFactory().getConnection()
                                      .incrBy(redisUtil.redisTemplate.getKeySerializer().serialize(key), i);
    }

    /**
     * 递增操作
     *
     * @param key 主键
     */
    public static long incr(String key) {
        return redisUtil.redisTemplate.opsForValue().increment(key, 1L);
    }

    /**
     * 超时
     */
    public static void expire(String key, long second) {
        expireSec(key, second);
    }

    /**
     * 超时(秒)
     */
    public static void expireSec(String key, long second) {
        redisUtil.redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    /**
     * 超时(分)
     */
    public static void expireMin(String key, long minute) {
        redisUtil.redisTemplate.expire(key, minute, TimeUnit.MINUTES);
    }

    /**
     * 超时(天)
     */
    public static void expireDay(String key, long day) {
        redisUtil.redisTemplate.expire(key, day, TimeUnit.DAYS);
    }

    public static boolean setNx(String key, Object val, long timeout) {
        return redisUtil.redisTemplate.opsForValue().setIfAbsent(key, val)
                && redisUtil.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 模糊查询keys
     */
    public static Set<String> keys(String prefix) {
        return (Set<String>) redisUtil.redisTemplate.keys(prefix);
    }

    /**
     * 根据keys获取值
     */
    public static List multiGet(Set<String> keys) {
        return redisUtil.redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * jedis 执行脚本
     *
     * @param script
     * @param keys
     * @param args
     * @return
     */
    public static Object eval(String script, List<String> keys, List<String> args) {
        return redisUtil.redisTemplate.execute((RedisCallback<Object>)connection -> {
            Object nativeConnection = connection.getNativeConnection();
            //            try {
            //                log.info("String===="+String.valueOf(nativeConnection.getClass()));
            //            }catch (Exception e){
            //                log.error("e"+ e.getMessage());
            //            }
            if (nativeConnection instanceof JedisCluster) {
                return ((JedisCluster) nativeConnection).eval(script, keys, args);
            }
            // 单点
            else if (nativeConnection instanceof Jedis) {
                return ((Jedis) nativeConnection).eval(script, keys, args);
            }
            return null;
        });
    }

    /**
     * 从Set获取变量指定区间的数据
     *
     * @param key
     * @return
     */
    public static Boolean isMember(String key, String value) {
        ZSetOperations<String, Object> zSetOperations = redisUtil.redisTemplate.opsForZSet();
        Double score = zSetOperations.score(key, value);
        if (score == null) {
            return false;
        }
        return true;
    }

}
