package com.zhf.service.impl;

import com.zhf.annotation.Timeout;
import com.zhf.aop.TimeoutAspect;
import com.zhf.service.TimeoutService;
import com.zhf.util.CombinationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: 曾鸿发
 * @create: 2022-04-15 16:33
 * @description：
 **/
@Service
public class TimeoutServiceImpl implements TimeoutService {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutServiceImpl.class);

    @Timeout(value = 5000)
    @Override
    public Object testTimeOut() {
        // System.out.println("开始执行优先方案！");
        logger.info("开始执行优先方案！");

        logger.error("{0}", "hello");

        List<List<String>> ret = new ArrayList<>();

        for (int i = 1; i < 30; i++) {
            List<String> nums = getNums(i);
            List<List<String>> subsets = CombinationUtil.subsets(nums);
            if (Thread.currentThread().isInterrupted()) {
                ret = new ArrayList<>();
                System.out.println("方法执行结束");
            }
            ret.addAll(subsets);
        }
        if (Thread.interrupted()) {
            System.out.println("仍然处于中断状态");
            return ret;
        }
        System.out.println("方法执行结束111");
        System.out.println(ret);
        return ret;

    }


    public static List<String> getNums(int n) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ret.add(UUID.randomUUID().toString());
        }
        return ret;
    }
}
