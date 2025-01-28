package com.zhf.util.pinin;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

public class PinInSortUtil {

    public static void sortByChinesePinIn1(List<String> list) {
        list.sort(new PinInComparator());
    }

    public static void sortByChinesePinIn2(List<String> list) {
        list.sort(new PinInLocaleComparator());
    }

    public static void sortByChinesePinIn3(List<String> list) {
        CollUtil.sortByPinyin(list);
    }

    private PinInSortUtil() {
    }
}
