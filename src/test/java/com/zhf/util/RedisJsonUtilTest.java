package com.zhf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhf.MyJavaFrameworkApplication;
import com.zhf.entity.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: 曾鸿发
 * @create: 2022-02-19 10:18
 * @description：
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class RedisJsonUtilTest {

    @Autowired
    RedisJsonUtil redisJsonUtil;

    @Test
    public void testSetAndGet(){
        Person person = new Person();
        person.setId(100001);
        person.setName("zhangsan");
        person.setAge(22);
        redisJsonUtil.putObjectToMap(person, person.getBussinessKey());
        Person p = redisJsonUtil.getMapToObject(person.getBussinessKey(), Person.class);
        System.out.println(p);
    }

    @Test
    public void testPubMessage(){
        redisJsonUtil.pubMessage("ooxx", "hello");
    }

}
