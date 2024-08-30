package com.zhf.job.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo1 implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        System.out.println( " " + sf.format(date) + " 任务1执行了，" + dataMap.getString("msb"));

    }

    public static void main(String[] args) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }

}