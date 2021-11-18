package com.zhf.aop;

import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @author: 曾鸿发
 * @create: 2021-11-18 11:56
 * @description：UserAgent Log配置类
 **/
@Aspect
@Component
@Slf4j
public class UserAgentAopLogConfig {

    private static final String START_TIME = "request-start";

    @Pointcut(value = "within(com.zhf.controller..*)")
    public void operationController() {
    }

    @Before("operationController()")
    public void beforeLog(JoinPoint point){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request= Objects.requireNonNull(attributes).getRequest();
        log.info("【请求URI】：{}", request.getRequestURI());
        log.info("【请求URL】：{}", request.getRequestURL());
        log.info("【请求 IP】: {}", request.getRemoteAddr());
        log.info("【请求类名】: {},【请求方法名】: {}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        Map<String,String[]> map= request.getParameterMap();
        log.info("【请求参数】: {}", JSONObject.toJSONString(map));
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
    }

    @After("operationController()")
    public void afterLog(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Long start= (Long) request.getAttribute(START_TIME);
        Long end=System.currentTimeMillis();
        log.info("【请求耗时】：{}ms",end-start);
        String header=request.getHeader("User-Agent");
        UserAgent userAgent=UserAgent.parseUserAgentString(header);
        log.info("【浏览器类型】：{},【操作系统】:{},【原始User-Agent】:{}",
                userAgent.getBrowser().toString(),
                userAgent.getOperatingSystem().toString(),
                header);
    }

}
