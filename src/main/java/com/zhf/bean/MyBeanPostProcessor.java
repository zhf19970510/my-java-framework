package com.zhf.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: 曾鸿发
 * @create: 2021-09-22 21:30
 * @description：BeanPostProcessor
 **/

// @Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在每一个对象初始化前面执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization:" + beanName);
        return bean;
    }

    /**
     * 在每一个对象初始化后面执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization:" + beanName);
        return bean;
    }
}
