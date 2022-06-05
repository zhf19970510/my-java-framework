package com.zhf.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: 曾鸿发
 * @create: 2022-06-01 16:51
 * @description：
 **/
public class RedissonController {

    @Autowired
    private Redisson redisson;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStock(){
        String lockKey = "product_001";
        RLock lock = redisson.getLock(lockKey);
        try{
            // 加锁，实现锁续命功能
            lock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock > 0){
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减成功，剩余库存：" + realStock);
            }else{
                System.out.println("扣减失败，库存不足！");
            }
        }finally {
            lock.unlock();
        }
        return "end";
    }
}
