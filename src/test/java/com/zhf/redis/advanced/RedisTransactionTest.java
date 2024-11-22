package com.zhf.redis.advanced;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisTransactionTest {

    @Autowired
    private RedisTransaction redisTransaction;

    @Test
    public void testTransaction() {
        redisTransaction.transaction();
    }

}