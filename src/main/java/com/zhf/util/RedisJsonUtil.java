package com.zhf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-02-19 08:35
 * @description：
 **/
@Component
public class RedisJsonUtil {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param t
     * @param businessKey
     * @param <T>
     */
    public <T> void putObjectToMap(T t, String businessKey) {
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        redisTemplate.opsForHash().putAll(businessKey, jm.toHash(t));
    }

    /**
     * @param businessKey
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getMapToObject(String businessKey, Class<T> clz) {
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        Map map = redisTemplate.opsForHash().entries(businessKey);
        return objectMapper.convertValue(map, clz);
    }

    public void putMapToHash(String businessKey, Map<String, String> map) {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.putAll(businessKey, map);
    }

    /**
     * pub sub
     */

    /**
     * 发布消息
     *
     * @param message
     */
    public void pubMessage(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    public boolean decreaseOptimise(String itemId) {

        try {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            // 这里是重点：
            Long decrement = ops.decrement(itemId.toString(), 1);
            if (decrement >= 0) {
                return true;
            } else {
                /**
                 * 数据的表示，编码
                 * 9,8,7,6,5,4,3,2,1,0 这样的数值是ascii
                 * x 0 0 0 0 0 0 0 X永远是0 0 ~ 127
                 * 如果我通过 ops.setBit(itemId.toString(), 0, true);
                 * 1 x x x x x x x 这个编码是错误的，按16进制给你返回，而且也不是int类型的
                 * 再操作desc 操作就会报异常
                 * 多个操作放到一个事务中执行
                 *
                 * 多个操作放到一个事务里
                 * 不会死锁
                 * 但是会出现异常，执行失败
                 * 连接池的概念，有可能通过不同的连接发出，
                 * 一个连接才是一个client，事务是基于client。
                 *
                 * 只有使用如下方式：SessionCallback的方式才不会有问题
                 */


                redisTemplate.setEnableTransactionSupport(true);
                // RedisCallback
                SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
                    @Override
                    public <K, V> Boolean execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                        // 事务开始
                        redisOperations.multi();

                        try {
                            ValueOperations ops = redisOperations.opsForValue();
                            ops.set(itemId.toString(), "0");
                            ops.setBit(itemId.toString(), 0, true);
                            // redisOperations.opsForValue().set();
                            // 事务提交或者回滚
                            redisOperations.exec();
                            return true;
                        } catch (Exception ex) {
                            redisOperations.discard();
                            return false;
                        }
                    }
                };
                redisTemplate.execute(callback);
            }
            redisTemplate.setEnableTransactionSupport(false);
        } catch (Exception e) {

        }
        return false;
    }
}
