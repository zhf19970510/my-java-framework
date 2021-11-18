package com.zhf.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // 执行目标方法
        Object ret = null;
        Stream<?> stream =  ArrayUtils.isEmpty(args) ? Stream.empty() : Stream.of(args);
        List<Object> logArgs = stream
                .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());
        log.info(signature.getName() + "==>param:" + JSON.toJSONString(logArgs));
        try {
            // 执行目标方法
            ret = joinPoint.proceed(args);
            try{
                log.info(signature.getName() + "==>result:" + ret);
            }catch (Exception e){
            }
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
