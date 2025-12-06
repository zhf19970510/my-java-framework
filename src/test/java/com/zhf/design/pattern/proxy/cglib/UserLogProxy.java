package com.zhf.design.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserLogProxy implements MethodInterceptor {

    /**
     * 生成cglib动态代理类方法
     * @param target    需要被代理的目标类
     * @return  代理类对象
     */
    public Object getLogProxy(Object target) {
        // 增强器类，用来创建动态代理类
        Enhancer enhancer = new Enhancer();

        // 设置代理类的父类字节码对象
        enhancer.setSuperclass(target.getClass());

        // 设置毁掉方法
        enhancer.setCallback(this);

        // 创建动态代理类对象，并返回
        return enhancer.create();
    }

    /**
     * 拦截方法:实现回调方法
     * @param o     代理类对象
     * @param method    被代理的方法：目标对象中的方法的Method实例
     * @param args   方法参数
     * @param methodProxy   方法代理类对象:代理对象中的方法的MethodProxy实例
     * @return  方法返回值
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始执行方法：" + method.getName() + "，时间：" + format.format(instance.getTime()));
        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("结束执行方法：" + method.getName() + "，时间：" + format.format(instance.getTime()));
        return result;
    }
}
