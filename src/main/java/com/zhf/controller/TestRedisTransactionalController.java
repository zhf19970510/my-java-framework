package com.zhf.controller;

import com.zhf.entity.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRedisTransactionalController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/redisTransaction")
    public BaseResult<String> redisTransaction() {
        redisTemplate.setEnableTransactionSupport(true);
        SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {
            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                // 事务开始
                redisTemplate.multi();

                try {
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
        return BaseResult.success();
    }
}
