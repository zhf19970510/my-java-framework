package com.zhf.util;

/**
 * @author: 曾鸿发
 * @create: 2021-09-14 16:26
 * @description：密码工具类
 **/
public class PasswordUtil {

    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //大写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //小写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
    /**
     * 密码必须是包含数字,字母,特殊字符组合校验
     * @param password
     * @return
     */
    public static boolean isContainsReasonablePassword(String password){
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE)){
            i++;
        }else{
            if (password.matches(REG_UPPERCASE)) i++;
        }
        if (password.matches(REG_SYMBOL)) i++;
        if (i  < 3 )  return false;
        return true;
    }

    public static boolean isSpecialCharacter(String s){
        return s.matches(".*[!@#^%&?$\"]+.*");
    }
}
