package com.zhf.util;

import cn.hutool.core.collection.CollectionUtil;
import com.zhf.entity.Device;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.zhf.util.SnowUtil.calculateAndGetSnowId;

public class SnowUtilTest {

    @Test
    public void testGetSnowId() {
        String snowId = calculateAndGetSnowId(new HashMap<>());
        System.out.println(snowId);

        Map<Device, String> map = new HashMap<>();
        Device device = new Device();
        device.setDeviceNumber("aaaaa");
        Device device1 = new Device();
        device1.setDeviceNumber("bbbbbbb");
        map.put(device, "hahaha");
        map.put(device1, "hahahahahaha");
        System.out.println(map.get(device));
        System.out.println(map.get(device1));

        Map<String, Device> deviceHashMap = new HashMap<>();
        Device device2 = deviceHashMap.computeIfAbsent("aa", t -> new Device());
        device2.setDeviceNumber("bbbb");
        System.out.println(deviceHashMap.get("aa").getDeviceNumber());
        Device device3 = deviceHashMap.computeIfAbsent("aa", t -> device1);
        device3.setDeviceNumber("new devicenumber");
        System.out.println(deviceHashMap.get("aa").getDeviceNumber());

        System.out.println((int) (Math.random() * 100));

        Map<String, String> map1 = new LinkedHashMap<>(100);
        for (int i = 0; i < 50; i++) {
            map1.put("i" + i, i * i + "");
        }

        System.out.println("=======================");
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            System.out.println(entry.getKey() + " ===== " + entry.getValue());
        }
        System.out.println("=======================");

        System.out.println(CollectionUtil.isEmpty(Collections.emptyMap()));
    }
}
