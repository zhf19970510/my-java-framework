package com.zhf.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 执行顺序：
 *      preHandler --->目标方法 --->postHandler --->先页面跳转 --->afterCompletion
 *          如果执行过程中出现异常，那么afterCompletion依然会继续执行执行，中间三步不会执行
 * @author: 曾鸿发
 * @create: 2021-10-24 16:22
 * @description：自定义拦截器
 **/
@Component
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 在处理器具体的方法之前开始执行，eg: 比如可以将请求中的Header中的session信息保存在session中
     * @param request
     * @param response
     * @param handler
     * @return          注意返回值，如果返回值是false表示请求处理到此为止，如果是true，才会接着向下执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(this.getClass().getName() + "-----preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(this.getClass().getName() + "-----postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(this.getClass().getName() + "-----afterCompletion");
        System.out.println(handler);
        System.out.println(handler.getClass());
    }
}
