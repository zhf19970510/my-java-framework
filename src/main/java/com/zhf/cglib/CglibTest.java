package com.zhf.cglib;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author: 曾鸿发
 * @create: 2021-10-17 20:09
 * @description： cglib测试类
 **/
public class CglibTest {

    public static void main(String[] args) {
        // 动态代理创建的class文件存储到本地
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY , "d:\\code");
        // 创建cglib获取代理对象的操作对象
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(MyCalculator.class);
        // 设置enhancer的回调对象
        enhancer.setCallback(new MyCglib());
        // 创建代理对象
        MyCalculator myCalculator = (MyCalculator) enhancer.create();
        myCalculator.add(1, 1);
        System.out.println(myCalculator.getClass());
    }
}
