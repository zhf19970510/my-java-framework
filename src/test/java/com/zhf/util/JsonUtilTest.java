package com.zhf.util;

import org.junit.Test;

import java.util.Map;

public class JsonUtilTest {

    @Test
    public void testParseJson() {
        String s1 ="{\"a\":{\"c\":\"d\",\"e\":\"f\"},\"b\":{\"3\":\"f\",\"f\":\"g\"}}";
        Map<String, Map<String, String>> map = JsonUtil.parseJsonToMap(s1);
        System.out.println(map);
    }
}
