package com.zhf.job;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

public class TestScheduledJob {

    @Scheduled(cron = "0 30,35,40,45,50 21 * * ? *")
    public void task() {
        System.out.println(new Date());
    }
}
