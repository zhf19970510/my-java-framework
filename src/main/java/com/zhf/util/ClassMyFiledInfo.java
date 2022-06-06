package com.zhf.util;

import com.zhf.annotation.MyField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author: 曾鸿发
 * @create: 2022-06-05 22:44
 * @description：
 **/
public class ClassMyFiledInfo {

    private Logger logger = LoggerFactory.getLogger(ClassMyFiledInfo.class);

    /**
     * 变量类型名，key是MyField的value
     */
    Map<Integer, String> fieldClassNames = new HashMap<>();

    /**
     * Getter方法，key是MyField的value
     */
    Map<Integer, Method> getters = new HashMap<>();

    /**
     * Setter方法，key是MyField的value
     */
    Map<Integer, Method> setters = new HashMap<>();

    public ClassMyFiledInfo(String className) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            for(Class<?> i = Class.forName(className); i != Object.class; i = i.getSuperclass()){
                fields.addAll(Arrays.asList(i.getDeclaredFields()));
            }
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

        fields.forEach(e -> {
            String name = e.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            MyField myField = e.getAnnotation(MyField.class);
            if(null != myField){
                try{
                    getters.put(myField.value(), clazz.getMethod("get" + name));
                    fieldClassNames.put(myField.value(), e.getType().getName());
                    setters.put(myField.value(), clazz.getMethod("set" + name, e.getType()));
                }catch (Exception ex){
                    logger.error(ex.getMessage(), ex);
                }
            }
        });
    }

    public Map<Integer, String> getFieldClassNames() {
        return fieldClassNames;
    }

    public void setFieldClassNames(Map<Integer, String> fieldClassNames) {
        this.fieldClassNames = fieldClassNames;
    }

    public Map<Integer, Method> getGetters() {
        return getters;
    }

    public void setGetters(Map<Integer, Method> getters) {
        this.getters = getters;
    }

    public Map<Integer, Method> getSetters() {
        return setters;
    }

    public void setSetters(Map<Integer, Method> setters) {
        this.setters = setters;
    }
}
