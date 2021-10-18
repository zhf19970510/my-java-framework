package com.zhf.factory;

import com.zhf.entity.Person;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author: 曾鸿发
 * @create: 2021-09-22 20:57
 * @description：
 **/
@Component
public class MyFactoryBean implements FactoryBean<Person> {
    /**
     * 返回获取的bean
     * @return
     * @throws Exception
     */
    @Override
    public Person getObject() throws Exception {
        Person person = new Person();
        person.setId(3);
        person.setName("张三");
        person.setAge(24);
        return person;
    }

    /**
     * 返回bean的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }

    /**
     * 判断当前bean是否是单例的
     * @return
     */
    @Override
    public boolean isSingleton() {
        return false;
    }
}
