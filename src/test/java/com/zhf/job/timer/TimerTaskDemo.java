package com.zhf.job.timer;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimerTask;

public class TimerTaskDemo extends TimerTask {
    /**
     * 此计时器任务要执行的操作。
     */
    public void run() {
        Date executeTime = new Date(this.scheduledExecutionTime());
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executeTime);
        System.out.println("任务执行了：" + dateStr);
    }


}
