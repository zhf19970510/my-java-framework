package com.zhf.controller;

import com.zhf.aop.TimeoutAspect;
import com.zhf.entity.base.BaseResult;
import com.zhf.service.TimeoutService;
import com.zhf.util.CombinationUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: 曾鸿发
 * @create: 2022-04-15 16:29
 * @description：
 **/
@Api(tags = "timeout测试接口")
@Slf4j
@RestController
@RequestMapping("/timeout")
public class TimeOutContrller {

    @Autowired
    private TimeoutService timeoutService;


    @Resource(name = "applicationTaskExecutor")
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(TimeOutContrller.class);

    @GetMapping("/testTimeout")
    public BaseResult testTimeOut() {
        Object object = timeoutService.testTimeOut();
        if (object instanceof List) {
            List<List<String>> ret = (List<List<String>>) object;
            return BaseResult.success("获取成功", ret.get(3));
        } else {
            return BaseResult.error("获取失败");
        }
    }

    @GetMapping("/timeoutTest")
    public BaseResult<String> timeoutTest() {
        Future<String> future = applicationTaskExecutor.submit(() -> {
            logger.info("timeoutTest 开始执行！");
            List<String> orderIdList = new ArrayList<>();
            for (int i = 0; i < 22; i++) {
                orderIdList.add("0" + i);
            }
            List<List<String>> subsets = CombinationUtil.subsets(orderIdList);
            System.out.println(subsets);
            System.out.println(subsets.size());
            return "success";
        });

        try {
            future.get(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("timeoutTest执行失败：线程 interrupted");
            return BaseResult.error("timeoutTest执行失败：线程 interrupted");
        } catch (ExecutionException e) {
            logger.error("timeoutTest执行失败");
            return BaseResult.error("timeoutTest执行失败");
        } catch (TimeoutException e) {
            logger.warn("timeoutTest执行超时，采用保底方案继续执行...");
            future.cancel(true);
        }
        return BaseResult.success("success");
    }
}
