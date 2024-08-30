package com.zhf.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTestQuartzJob1 implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println(" " + sdf.format(date) + " 任务1执行了，" + jobDataMap.getString("zhf"));
    }

    public static void main(String[] args) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }
}
