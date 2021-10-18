package com.zhf.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 09:57
 * @description：日志配置类
 **/

@Aspect
@Component
@Slf4j
public class LogAopConfig {

    @Pointcut(value = "within(com.zhf.controller..*)")
    public void operationController() {
    }

    @Around("operationController()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        log.info(signature.getName() + "==>param:" + JSON.toJSONString(args));
        // 执行目标方法
        Object ret = null;
        try {
            // 执行目标方法
            ret = joinPoint.proceed(args);
            log.info(signature.getName() + "==>result:" + JSON.toJSONString(ret));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
        return ret;
    }

    @AfterThrowing(pointcut = "operationController()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error(joinPoint.getSignature().getName() + "==>error:" + ex.getMessage());
    }
}
