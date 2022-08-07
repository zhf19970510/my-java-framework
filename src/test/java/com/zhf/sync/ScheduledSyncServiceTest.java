package com.zhf.sync;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
public class ScheduledSyncServiceTest {

    @Test
    public void testScheduledSyncService() {
        ScheduledSyncService scheduledSyncService = new ScheduledSyncService();
        ScheduledThread scheduledThread = new ScheduledThread(scheduledSyncService);
        ScheduleLifeCycleThread scheduleLifeCycleThread = new ScheduleLifeCycleThread(scheduledSyncService);
        scheduledThread.start();
        System.out.println("=======================");
        ThreadUtil.sleep(10, TimeUnit.SECONDS);
        scheduleLifeCycleThread.start();
    }

}
