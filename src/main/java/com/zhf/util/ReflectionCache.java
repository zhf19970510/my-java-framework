package com.zhf.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 曾鸿发
 * @create: 2022-06-05 22:59
 * @description：
 **/
public class ReflectionCache {
    private static Map<String, ClassMyFiledInfo> classMyFiledInfoMap = new ConcurrentHashMap<>();

    public static ClassMyFiledInfo getClassMyFiledInfo(String className) {
        if(!classMyFiledInfoMap.containsKey(className)){
            classMyFiledInfoMap.put(className, new ClassMyFiledInfo(className));
        }
        return classMyFiledInfoMap.get(className);
    }
}
