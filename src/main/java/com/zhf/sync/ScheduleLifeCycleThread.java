package com.zhf.sync;

public class ScheduleLifeCycleThread extends Thread {

    private ScheduledSyncService scheduledSyncService;

    public ScheduleLifeCycleThread(ScheduledSyncService scheduledSyncService) {
        this.scheduledSyncService = scheduledSyncService;
    }

    @Override
    public void run() {
        scheduledSyncService.closeSchedule();
    }
}
