package com.zhf.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2022-04-15 11:33
 * @description：
 **/
public class CombinationUtil {

    public static List<List<String>> subsets(List<String> nums) {

        boolean[] flags = new boolean[nums.size()];
        List<List<String>> rst = new ArrayList<>();
        addSubSets(flags, 0, nums.size() - 1, nums, rst);
        return rst;
    }

    private static void addSubSets(boolean[] flags, int start, int end, List<String> nums, List<List<String>> rst) {
        if (!Thread.currentThread().isInterrupted()) {
            // System.out.println("addSets...");
            if (start == end) {
                flags[start] = true;
                add(rst, flags, nums);
                flags[start] = false;
                add(rst, flags, nums);
            } else {
                flags[start] = true;
                addSubSets(flags, start + 1, end, nums, rst);
                flags[start] = false;
                addSubSets(flags, start + 1, end, nums, rst);
            }
        }


    }

    private static void add(List<List<String>> rst, boolean[] flags, List<String> nums) {
        List<String> one = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            // 让当前线程从运行状态转到就绪状态，以允许具有相同优先级的其他线程获得运行机会，让出cpu，防止这个线程一直占用cpu
            Thread.yield();
            if (flags[i]) {
                one.add(nums.get(i));
            }
        }
        if (one.size() > 0) {
            rst.add(one);
        }
    }

}
