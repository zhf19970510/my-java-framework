package com.zhf.social;

import java.util.ArrayList;
import java.util.List;

public class social {
    public static void main(String[] args) {
        List<Double> list = new ArrayList<>();
        list.add(832.80);
        list.add(832.80);
        list.add(832.80);
        list.add(832.80);
        list.add(832.80);

        list.add(832.80);
        list.add(832.80);
        list.add(832.80);
        list.add(832.80);
        list.add(394.20);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(478.00);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(521.60);
        list.add(584.80);
        list.add(584.80);
        list.add(584.80);
        list.add(584.80);
        list.add(584.80);
        list.add(584.80);
        list.add(584.80);
        System.out.println("到2024年2月工资截止：工资缴纳情况如下");
        System.out.println("工缴纳月份：" + list.size());
        Double total = 0.0;
        for (Double aDouble : list) {
            total += aDouble;
        }
        System.out.println(total);
    }
}
