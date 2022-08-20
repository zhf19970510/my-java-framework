package com.zhf.util;

import com.zhf.enums.ErrorEnum;
import com.zhf.exception.RenException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JobUtil {
    private static final String DEFAULT_JOB_GROUP = "DEF_JOB_GROUP";
    private static final String DEFAULT_TRIGGER_GROUP = "DEF_TRI_GROUP";
    @Autowired
    private Scheduler scheduler;

    public JobUtil() {
        //something
    }

    private JobKey getJobKeyByClazz(Class<? extends Job> clazz) {
        return new JobKey(clazz.getName(), DEFAULT_JOB_GROUP);
    }

    private TriggerKey getTriggerKeyByClazz(Class<? extends Job> clazz) {
        return new TriggerKey("TRI4" + clazz.getName(), DEFAULT_TRIGGER_GROUP);
    }

    public void addJob(Class<? extends Job> clazz, String cron, Date startAt, Map<String, Object> paramMap) throws SchedulerException {
        if (!CronExpression.isValidExpression(cron)) {
            throw new RenException(ErrorEnum.CRON_EXPRESSION);
        } else {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(clazz.getName(), DEFAULT_JOB_GROUP).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("TRI4" + clazz.getName(), DEFAULT_TRIGGER_GROUP).startAt(startAt == null ? new Date() : startAt).withSchedule(scheduleBuilder).build();
//            SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("TRI4" + clazz.getName(), DEFAULT_TRIGGER_GROUP).startAt(startAt == null ? new Date() : startAt).withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0)).build();
            trigger.getJobDataMap().putAll(paramMap);
            //判断任务是否存在
            if (!this.scheduler.checkExists(this.getJobKeyByClazz(clazz))) {
                this.scheduler.scheduleJob(jobDetail, trigger);
            } else {
                throw new RenException(ErrorEnum.JOB_EXISTS);
            }
        }
    }

    public boolean checkExists(Class<? extends Job> clazz) throws SchedulerException {
        try {
            return this.scheduler.checkExists(this.getJobKeyByClazz(clazz));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean checkExists(String beanName) throws SchedulerException {
        return checkExists((Class<? extends Job>) SpringUtil.getBean(beanName).getClass());
    }


    public String getJobState(Class<? extends Job> clazz) throws SchedulerException {
        return this.scheduler.getTriggerState(this.getTriggerKeyByClazz(clazz)).name();
    }

    public void pauseAllJob() throws SchedulerException {
        this.scheduler.pauseAll();
    }

    public void pauseJob(Class<? extends Job> clazz) throws SchedulerException {
        this.scheduler.pauseJob(this.getJobKeyByClazz(clazz));
    }

    public void pauseJob(String beanName) throws SchedulerException {
        pauseJob((Class<? extends Job>) SpringUtil.getBean(beanName).getClass());
    }

    public void resumeAllJob() throws SchedulerException {
        this.scheduler.resumeAll();
    }

    public void resumeJob(Class<? extends Job> clazz) throws SchedulerException {
        this.scheduler.resumeJob(this.getJobKeyByClazz(clazz));
    }

    public void resumeJob(String beanName) throws SchedulerException {
        resumeJob((Class<? extends Job>) SpringUtil.getBean(beanName).getClass());
    }

    public void deleteJob(Class<? extends Job> clazz) throws SchedulerException {
        this.scheduler.deleteJob(this.getJobKeyByClazz(clazz));
    }

    public void modifyJob(Class<? extends Job> clazz, String cron, Date startAt, Map<String, Object> paramMap) throws SchedulerException {
        if (!CronExpression.isValidExpression(cron)) {
            throw new RenException(ErrorEnum.CRON_EXPRESSION);
        } else {
            TriggerKey triggerKey = this.getTriggerKeyByClazz(clazz);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(startAt == null ? new Date() : startAt).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().putAll(paramMap);
            this.scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public Set<JobKey> listJobKey() throws SchedulerException {
        return this.scheduler.getJobKeys(GroupMatcher.anyGroup());
    }

    public Set<String> listJobName() throws SchedulerException {
        return this.scheduler.getJobKeys(GroupMatcher.anyGroup()).stream().map(Key::getName).collect(Collectors.toSet());
    }
}
