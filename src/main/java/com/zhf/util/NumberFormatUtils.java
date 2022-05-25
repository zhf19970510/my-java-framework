package com.zhf.util;

import com.zhf.exception.ServiceException;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @author: 曾鸿发
 * @create: 2022-03-02 14:47
 * @description：
 **/
public class NumberFormatUtils {

    /**
     * 将数字格式化成百分数
     *
     * @param obj
     * @return
     */
    public static String format(Object obj) {
        if (obj instanceof Number) {
            NumberFormat nf = NumberFormat.getInstance();
            // 设置最长小数位数
            nf.setMaximumFractionDigits(30);
            // 不用科学计数
            nf.setGroupingUsed(false);
            obj = nf.format(obj);
        }
        if (obj == null || !String.valueOf(obj).matches("\\-*\\d+(\\.\\d+)*")) {
            throw new ServiceException("格式化百分数格式错误 obj:" + obj);
        }

        BigDecimal temp = new BigDecimal(String.valueOf(obj));
        return temp.multiply(BigDecimal.valueOf(100)).setScale(temp.scale() - 2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%";
    }

    /**
     * 千分符转化
     *
     * @param object
     * @return
     */
    public static String millmeterFormat(Object object) {
        BigDecimal number = null;
        if (object instanceof String && NumberUtils.isNumber(String.valueOf(String.valueOf(object)))) {
            String objectString = (String) object;
            try {
                if (objectString.contains(",")) {
                    objectString = objectString.replaceAll(",", "");
                }
                number = new BigDecimal(objectString);
            } catch (Exception ex) {
                throw new ServiceException("字符串格式不符合数字格式 number:" + objectString);
            }
        } else if (object instanceof NumberUtils) {
            number = new BigDecimal(object.toString());
        } else {
            throw new ServiceException("千分位转换不支持的数据格式，待转换的值：" + object.toString());
        }

        NumberFormat format = NumberFormat.getInstance();
        // 设置数值的【小数部分】允许的最大位数
        format.setMaximumFractionDigits(20);
        return format.format(number);
    }

}
