package com.zhf;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author: 曾鸿发
 * @create: 2022-03-22 12:39
 * @description：
 **/
public class TimeZoneTest {

    public static void main(String[] args) {
        TimeZone tz = TimeZone.getTimeZone("GMT+08:00");
        // 获取“id”
        String id = tz.getID();
        // 获取“显示名称”
        String name = tz.getDisplayName();
        // 获取“时间偏移”。相对于“本初子午线”的偏移，单位是ms。
        int offset = tz.getRawOffset();
        // 获取“时间偏移” 对应的小时
        int gmt = offset/(3600*1000);
        System.out.printf("id=%s, name=%s, offset=%s(ms), gmt=%s\n", id, name, offset, gmt);
    }
}
