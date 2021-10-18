package com.zhf.cglib;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: 曾鸿发
 * @create: 2021-10-17 19:51
 * @description： cglib代理类
 **/
public class MyCglib implements MethodInterceptor {

    /**
     * 此方法用来实现方法的拦截
     * @param obj                   增强的对象，表示实现这个接口类的一个对象
     * @param method                要拦截的方法
     * @param args                  拦截的方法的参数
     * @param proxy                 要触发父类的方法对象
     * @return
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("方法执行之前...");
        Object o = proxy.invokeSuper(obj, args);
        System.out.println("方法执行之后...");
        return o;
    }
}
