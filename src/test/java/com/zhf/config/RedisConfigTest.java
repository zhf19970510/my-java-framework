package com.zhf.config;

import com.zhf.MyJavaFrameworkApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: 曾鸿发
 * @create: 2022-06-10 12:38
 * @description：
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class RedisConfigTest {

    @Autowired
    private RedisTemplate redisTemplate;

}
