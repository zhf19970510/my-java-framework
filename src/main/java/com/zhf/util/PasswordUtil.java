package com.zhf.util;

import com.zhf.exception.ServiceException;

import java.security.MessageDigest;

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
     *
     * @param password
     * @return
     */
    public static boolean isContainsReasonablePassword(String password) {
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE)) {
            i++;
        } else {
            if (password.matches(REG_UPPERCASE)) i++;
        }
        if (password.matches(REG_SYMBOL)) i++;
        if (i < 3) return false;
        return true;
    }

    public static boolean isSpecialCharacter(String s) {
        return s.matches(".*[!@#^%&?$\"]+.*");
    }

    /**
     * 密码加密
     * @param key
     * @param data
     * @return
     */
    public static String passwordEncode(String key, String data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] dataArray = data.getBytes("UTF-8");
            byte[] keyArray = key.getBytes("UTF-8");

            md5.update(keyArray);
            byte[] md5key = md5.digest();

            sha.update(dataArray);
            sha.update(md5key);
            byte[] shaBytes = sha.digest();

            for(int j = 0; j < 5000; j++){
                sha.update(shaBytes);
                shaBytes = sha.digest();
            }

            StringBuffer hexValue = new StringBuffer();

            for(int i = 0; i < shaBytes.length; i++){
                int val = ((int) shaBytes[i]) & 0xff;
                if(val < 16){
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();

        }catch (Exception e){
            throw new ServiceException("密码加密错误！");
        }
    }

}
