package com.zhf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class LuaConfiguration {

    @Bean(name = "redisSetScript")
    public DefaultRedisScript<Boolean> redisSetScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Boolean.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/redis-set.lua")));
        return redisScript;
    }

    // java代码执行redis lua脚本案例
    /*
    // 设置加锁的key,设置过期时间，避免死锁 -lua
        List<String> strings = Arrays.asList(key, value);
        Boolean aBoolean = stringRedisTemplate.execute(redisSetScript, strings, "30");
     */
}
