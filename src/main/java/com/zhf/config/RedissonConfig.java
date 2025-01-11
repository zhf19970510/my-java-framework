package com.zhf.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

/**
 * @author: 曾鸿发
 * @create: 2022-06-01 16:15
 * @description：
 **/
//@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);

//        config.setLockWatchdogTimeout(30);  // 默认情况下，看门狗检查锁的超时时间是30秒
        /*config.useClusterServers()
                .addNodeAddress("redis://192.168.199.141:8001")
                .addNodeAddress("redis://192.168.199.161:8002")
                .addNodeAddress("redis://192.168.199.171:8003")
                .addNodeAddress("redis://192.168.199.141:8004")
                .addNodeAddress("redis://192.168.199.161:8005")
                .addNodeAddress("redis://192.168.199.171:8006");*/
        return (Redisson) Redisson.create(config);
    }
}
