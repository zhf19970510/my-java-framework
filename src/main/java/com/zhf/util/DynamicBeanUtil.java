package com.zhf.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DynamicBeanUtil {

    @Autowired
    private ApplicationContext applicationContext;

    // 添加bean
    public void addBean(String beanName, Class<?> beanClass) {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        if (!beanDefinitionRegistry.containsBeanDefinition(beanName)) {
           beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    // 移除bean
    public void removeBean(String beanName){
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        beanDefinitionRegistry.getBeanDefinition(beanName);
        beanDefinitionRegistry.removeBeanDefinition(beanName);
    }

    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }

}
