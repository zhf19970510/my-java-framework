package com.zhf.util.com.zhf.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhf.entity.DecimalTest;
import com.zhf.entity.Student;
import com.zhf.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
public class DateUtilTest {

    @Test
    public void testDateToLocalDate(){
        Date date = new Date();
        System.out.println(DateUtil.dateToLocalDate(date));
        System.out.println(DateUtil.dateToLocalDate2(date));

        byte a = (byte) (123 +6);
        System.out.println(a);

//        com.alibaba.fastjson.JSONObject

        Student student = new Student();
        student.setBirthday(new Date());
        student.setId(123);
        student.setName("aaaa");
        String s = JSON.toJSONString(student);
        System.out.println(s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        System.out.println("id: " + id);
        String birthday = jsonObject.getString("birthday");
        System.out.println("birthday： " + birthday);

        DecimalTest decimalTest = new DecimalTest();
        decimalTest.setAge(new BigDecimal("22.345"));
        decimalTest.setId(123);
        String jsonString = JSON.toJSONString(decimalTest);
        System.out.println(jsonString);
        JSONObject jsonObject1 = JSONObject.parseObject(jsonString);
        BigDecimal age = jsonObject1.getBigDecimal("age");
        System.out.println(age);

        BigDecimal bigDecimal = new BigDecimal("123");
        System.out.println(String.valueOf(bigDecimal));
//        System.out.println(String.valueOf(null));
        String s2 = "aa" + null + "cc";
        System.out.println(s2);

    }

}
