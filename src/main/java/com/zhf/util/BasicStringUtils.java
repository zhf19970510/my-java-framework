package com.zhf.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 曾鸿发
 * @create: 2022-03-01 20:41
 * @description：
 **/
public class BasicStringUtils {

    /**
     * 过滤输入字符串
     *
     * @param ori          原始字符串
     * @param invalidChars 不合法的字符集
     * @return 结果不含列表中的字符串
     */
    public static String filterOut(String ori, char[] invalidChars) {
        if (ori == null) {
            return null;
        }
        final char[] array = ori.toCharArray();
        final StringBuilder builder = new StringBuilder();
        for (final char ch : array) {
            boolean valid = true;
            for (int j = 0; j < invalidChars.length; j++) {
                if (ch == invalidChars[j]) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
