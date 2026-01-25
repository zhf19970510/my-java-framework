package com.zhf.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestScheduledJob {

    @Scheduled(cron = "0 30,35,40,45,50 21 * * ?")
    public void task() {
        System.out.println(new Date());
    }


    // 每分钟：0 0/1 * * * ?
    // 下面的是每秒钟
    @Scheduled(cron = "* * * * * ?")
    public void task1() {
        System.out.println(new Date());
    }
}
