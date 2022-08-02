package com.zhf.util;

import com.zhf.MyJavaFrameworkApplication;
import com.zhf.entity.MyDynamicBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class DynamicBeanUtilTest {

    @Autowired
    private DynamicBeanUtil dynamicBeanUtil;

    @Test
    public void testAddBean(){
        ApplicationContext applicationContext = dynamicBeanUtil.getApplicationContext();
        try{
            MyDynamicBean myDynamicBean = applicationContext.getBean("MyDynamicBean", MyDynamicBean.class);
            System.out.println(myDynamicBean);
        }catch (Exception e){
            e.printStackTrace();
        }

        dynamicBeanUtil.addBean("MyDynamicBean", MyDynamicBean.class);
        MyDynamicBean myDynamicBean1 = applicationContext.getBean("MyDynamicBean", MyDynamicBean.class);
        System.out.println(myDynamicBean1);
    }

}
