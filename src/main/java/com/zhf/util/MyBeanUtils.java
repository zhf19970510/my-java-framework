package com.zhf.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author: 曾鸿发
 * @create: 2022-03-01 21:46
 * @description：
 **/
@Slf4j
public class MyBeanUtils {

    static final String TYPE_STRING = "java.lang.String";

    public static Object getFieldValue(Object bean, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        String methodName = builder.append("get").append(StringUtils.capitalize(fieldName)).toString();

        Class<?>[] classArray = new Class[0];
        Method method = bean.getClass().getMethod(methodName, classArray);
        Object result = method.invoke(bean, new Object[0]);
        return result;
    }

    public static void setFieldValue(Object bean, String fieldName, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        StringBuilder builder = new StringBuilder();
        String methodName = builder.append("set").append(StringUtils.capitalize(fieldName)).toString();
        Class<?>[] classArr = new Class[1];
        classArr[0] = TYPE_STRING.getClass();
        Method method = bean.getClass().getMethod(methodName, classArr);
        method.invoke(bean, value);
    }

    public static Map<String, Object> objectToMap(Object data){
        Field[] fields = data.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for(Field f : fields){
            int modifiers = f.getModifiers();
            if(Modifier.isStatic(modifiers)){
                continue;
            }
            try {
                map.put(f.getName(), PropertyUtils.getProperty(data, f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception ex){
                log.error("Error get member {}", f.getName(), ex);
            }
        }
        return map;
    }

    /**
     * <p> list po to map<key, T> </p>
     * @param incoming
     * @param keyGenerator
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> listToMap(List<T>incoming, Function<T, String> keyGenerator){
        Map<String, T> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(incoming)){
            for(T datum : incoming){
                String key = keyGenerator.apply(datum);
                map.put(key, datum);
            }
        }
        return map;
    }

    /**
     * 将list po 转换为 map<key, List<PO>
     * @param incoming
     * @param keyGenerator
     * @param <T>
     * @return
     */
    public static <T> Map<String, List<T>> group(List<T> incoming, Function<T, String> keyGenerator){
        Map<String, List<T>> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(incoming)){
            for(T datum : incoming){
                String key = keyGenerator.apply(datum);
                List<T> list = map.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(datum);
            }
        }
        return map;
    }
}
