package com.zhf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 曾鸿发
 * @create: 2022-04-15 14:24
 * @description：
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean("applicationTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int i = Runtime.getRuntime().availableProcessors();     // 获取到的服务器cpu内核
        executor.setCorePoolSize(2 * i);                        // 核心池大小
        executor.setMaxPoolSize(100);                           // 最大线程数
        executor.setThreadNamePrefix("task-async" + i);         // 线程前缀名称
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());     // 配置拒绝策略
        return executor;
    }

}
