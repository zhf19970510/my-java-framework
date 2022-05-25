package com.zhf.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author: 曾鸿发
 * @create: 2022-02-12 18:01
 * @description：
 **/
public class FatherToChildUtils {

    public static boolean fatherToChild(Object father, Object child){
        if(child.getClass().getSuperclass() != father.getClass()){
            return false;
        }
        Class<?> fatherClass = father.getClass();
        Field[] ff = fatherClass.getDeclaredFields();
        for (Field field : ff) {
            try {
                if(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                StringUtils.equals("aa", "bb");
                Method method = fatherClass.getMethod("get" + upperHeadChar(field.getName()));
                if (method == null) {
                    continue;
                }
                Object value = method.invoke(father);
                field.set(child, value);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                continue;
            }
        }
        return true;

    }

    private static String upperHeadChar(String in){
        String head = in.substring(0, 1);
        return head.toUpperCase() + in.substring(1);
    }
}
