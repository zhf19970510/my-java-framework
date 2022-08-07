package com.zhf.sync;

public class ScheduledThread extends Thread {

    private ScheduledSyncService scheduledSyncService;

    public ScheduledThread(ScheduledSyncService scheduledSyncService) {
        this.scheduledSyncService = scheduledSyncService;
    }

    @Override
    public void run() {
        scheduledSyncService.schedule();
    }
}
