package com.zhf.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static Map parseJsonToMap(String jsonStr) {
        try{
            Map map = (Map) parseJson(jsonStr, LinkedHashMap.class);
            if(CollectionUtil.isEmpty(map)){
                return new LinkedHashMap();
            }
            return map;
        }catch (Exception ex){
            logger.error("Failed to parse to Map", ex);
        }

        return null;
    }

    public static Object parseJson(String jsonStr, Class<?> clazz) throws Exception {
        Object result = JSONObject.parseObject(jsonStr, clazz);
        if (result == null) {
            return clazz.newInstance();
        }
        if (Map.class == clazz || LinkedHashMap.class == clazz || HashMap.class == clazz) {
            Map mapResult = (Map) result;
            for (Object k : mapResult.keySet()) {
                String v = JSON.toJSONString(mapResult.get(k));

                if (StringUtils.isEmpty(v)) {
                    mapResult.put(k, "");
                    continue;
                }
                if (v.charAt(0) == '[') {
                    mapResult.put(k, parseJson(v, ArrayList.class));
                } else if (v.charAt(0) == '{') {
                    mapResult.put(k, parseJson(v, LinkedHashMap.class));
                } else {
                    mapResult.put(k, parseJson(v, String.class));
                }
            }
            return mapResult;
        } else if (List.class == clazz || ArrayList.class == clazz) {
            List listResult = (List) result;
            for (int i = 0; i < listResult.size(); i++) {
                String v = JSON.toJSONString(listResult.get(i));

                if (StringUtils.isEmpty(v)) {
                    listResult.set(i, "");
                    continue;
                }

                if (v.charAt(0) == '[') {
                    listResult.set(i, parseJson(v, ArrayList.class));
                } else if (v.charAt(0) == '{') {
                    listResult.set(i, parseJson(v, LinkedHashMap.class));
                } else {
                    listResult.set(i, String.class);
                }
            }
            return listResult;
        } else {
            return result;
        }
    }

}
