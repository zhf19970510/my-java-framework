package com.zhf.design.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂类 - 动态地生成代理对象
 */
public class ProxyFactory {

    // 维护一个目标对象
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    // 为目标对象生成代理对象
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(
                // 目标类使用的类加载器
                target.getClass().getClassLoader(),
                // 目标对象实现的接口类型
                target.getClass().getInterfaces(),
                // 代理对象对应的处理器
                new InvocationHandler() {
                    /**
                     *
                     * @param proxy 代理对象
                     *
                     * @param method 对应于在代理对象上调用接口方法实例
                     *
                     * @param args 对应了代理对象在调用接口方法时传递的实际参数
                     *
                     * @return 返回目标对象的返回值，没有返回值，就返回null
                     * @throws Throwable
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        System.out.println("开始执行代理方法");
                        Object returnValue = method.invoke(target, args);
                        System.out.println("结束执行代理方法");
                        return null;
                    }
                }
        );
    }
}
