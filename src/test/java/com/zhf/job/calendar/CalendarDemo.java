package com.zhf.job.calendar;

import com.zhf.job.quartz.Demo1;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarDemo {
    public static void main(String[] args) throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();
        scheduler.start();

        // 定义日历
        AnnualCalendar holidays = new AnnualCalendar();

        // 排除涛哥生日
        Calendar day = (Calendar) new GregorianCalendar(2021, 1, 1);
        holidays.setDayExcluded(day, true);
        // 排除中秋节
        Calendar midAutumn = new GregorianCalendar(2022, 9, 13);
        holidays.setDayExcluded(midAutumn, true);
        // 排除圣诞节
        Calendar christmas = new GregorianCalendar(2022, 12, 25);
        holidays.setDayExcluded(christmas, true);

        // 调度器添加日历
        scheduler.addCalendar("holidays", holidays, false, false);

        JobDetail jobDetail = JobBuilder.newJob(Demo1.class)
                .withIdentity("job1", "group1")
                .usingJobData("msb","涛哥")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .modifiedByCalendar("holidays")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .build();

        Date firstRunTime = scheduler.scheduleJob(jobDetail, trigger);
        System.out.println(jobDetail.getKey() + " 第一次触发： " + firstRunTime);
    }
}
