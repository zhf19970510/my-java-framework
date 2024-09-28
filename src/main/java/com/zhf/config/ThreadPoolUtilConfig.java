package com.zhf.config;

import com.zhf.util.ThreadPoolUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolUtilConfig {

    @Value("${thread.util.num}")
    private Integer threadUtilNum;

    @Value("${thread.util.divide}")
    private Integer threadUtilDivideNum;

    @Bean
    public ThreadPoolUtil threadPoolUtil() {
        ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil(threadUtilNum, threadUtilDivideNum);
        return threadPoolUtil;
    }
}
