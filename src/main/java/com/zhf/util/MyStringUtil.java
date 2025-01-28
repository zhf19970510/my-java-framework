package com.zhf.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class MyStringUtil {

    /**
     * @param data       要转换的具体数值
     * @param defaultStr 默认值
     * @return
     */
    public static String getStringByObjectIfNull(Object data, String defaultStr) {
        if (data == null) {
            return defaultStr;
        }
        return data.toString();
    }

    public static String getStringByObjectIfNullWithMaxLength(Object data, String defaultStr, int maxLength) {
        if (data == null) {
            return defaultStr;
        }

        String temp = data.toString();
        if (temp.length() > maxLength) {
            return temp.substring(0, maxLength);
        }
        return temp;
    }

    public static BigDecimal getBigDecimalByObjectIfNull(Object data, BigDecimal defaultDec) {
        if (data == null || StringUtils.isBlank(data.toString())) {
            return defaultDec;
        }
        return new BigDecimal(data.toString());
    }

    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        return value instanceof String ? (String) value : value.toString();
    }
}
