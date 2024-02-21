package com.zhf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyNumberUtil {

    public static String getPercent(int totalSize, int part){
        String percent = "0.0%";
        if(totalSize > 0){
            BigDecimal bigDecimal = new BigDecimal((part * 1.0 / totalSize) * 100);
            percent = bigDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
        }
        return percent;
    }

    public static long getADivideBAndMultiplyC(Long a, Long b, Long c) {
        return ((Double) (1.0 * a / b * c)).longValue();
    }
}
