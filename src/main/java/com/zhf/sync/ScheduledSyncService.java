package com.zhf.sync;

import org.springframework.scheduling.annotation.Async;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledSyncService {
    private final ScheduledExecutorService scheduleService = Executors.newSingleThreadScheduledExecutor();
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean closeScheduleJob;
    public ScheduledSyncService() {
    }
    public void schedule(){
        scheduleService.scheduleAtFixedRate(this::handle, 1, 2, TimeUnit.SECONDS);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Async
    public void handle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));
        if(closeScheduleJob){
            latch.countDown();
        }
    }
    @Async
    public void closeSchedule() {
        closeScheduleJob = true;
    }

}
