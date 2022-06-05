package com.zhf.config;

import com.zhf.MyJavaFrameworkApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2022-04-19 18:32
 * @description：
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class ParsingConfigTest {

    @Autowired
    private ParsingConfig parsingConfig;

    @Test
    public void testParsingConfig(){
        BigDecimal groupBookingAmountTolerance = parsingConfig.getGroupBookingAmountTolerance();
        System.out.println(groupBookingAmountTolerance);
        System.out.println(groupBookingAmountTolerance.toPlainString());
        System.out.println(groupBookingAmountTolerance.stripTrailingZeros().toPlainString());
        System.out.println("======================================");
        BigDecimal bigDecimal = new BigDecimal("100.234");
        BigDecimal bigDecimal1 = new BigDecimal("100.234");
        System.out.println(bigDecimal.equals(bigDecimal1));
        System.out.println(bigDecimal.toPlainString().equals(bigDecimal1.toPlainString()));
        System.out.println(bigDecimal.stripTrailingZeros().toPlainString().equals(bigDecimal1.stripTrailingZeros().toPlainString()));

        BigDecimal upper = bigDecimal.add(new BigDecimal("0"));
        BigDecimal lower = bigDecimal.subtract(new BigDecimal("0"));
        System.out.println(upper.toPlainString());
        System.out.println(lower.toPlainString());

        System.out.println(upper.compareTo(bigDecimal1));
        System.out.println(lower.compareTo(bigDecimal1));

        System.out.println(bigDecimal1.compareTo(upper) <= 0);
        System.out.println(bigDecimal1.compareTo(lower) >= 0);

        List<Object> list = new ArrayList<>();

    }
}
