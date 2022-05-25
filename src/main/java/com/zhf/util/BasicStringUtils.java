package com.zhf.util;

/**
 * @author: 曾鸿发
 * @create: 2022-03-01 20:41
 * @description：
 **/
public class BasicStringUtils {

    /**
     * 过滤输入字符串
     * @param ori               原始字符串
     * @param invalidChars      不合法的字符集
     * @return                  结果不含列表中的字符串
     */
    public static String filterOut(String ori, char[] invalidChars){
        if(ori == null){
            return null;
        }
        final char[] array = ori.toCharArray();
        final StringBuilder builder = new StringBuilder();
        for(final char ch : array){
            boolean valid = true;
            for(int j = 0; j < invalidChars.length; j++){
                if(ch == invalidChars[j]){
                    valid = false;
                    break;
                }
            }
            if(valid){
                builder.append(ch);
            }
        }
        return builder.toString();
    }


}
