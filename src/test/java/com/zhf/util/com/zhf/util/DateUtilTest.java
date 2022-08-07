package com.zhf.util.com.zhf.util;

import com.zhf.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class DateUtilTest {

    @Test
    public void testDateToLocalDate(){
        Date date = new Date();
        System.out.println(DateUtil.dateToLocalDate(date));
        System.out.println(DateUtil.dateToLocalDate2(date));
    }

}
