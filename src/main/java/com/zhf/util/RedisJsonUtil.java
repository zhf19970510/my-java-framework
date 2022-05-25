package com.zhf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
     *
     * @param t
     * @param businessKey
     * @param <T>
     */
    public <T> void putObjectToMap(T t, String businessKey){
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        redisTemplate.opsForHash().putAll(businessKey, jm.toHash(t));
    }

    /**
     *
     * @param businessKey
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getMapToObject(String businessKey, Class<T> clz){
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        Map map = redisTemplate.opsForHash().entries(businessKey);
        return objectMapper.convertValue(map, clz);
    }

    public void putMapToHash(String businessKey, Map<String, String> map){
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.putAll(businessKey, map);
    }

    /**
     * pub sub
     */

    /**
     * 发布消息
     * @param message
     */
    public void pubMessage(String channel , String message){
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
