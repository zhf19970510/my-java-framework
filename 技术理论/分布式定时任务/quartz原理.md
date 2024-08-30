## 任务调度之Quartz

线程调度模型：

5个动作：
依赖
配置
调度器
任务
触发器

![image-20240801210619506](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408012123299.png)

![image-20240801213711409](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408012137658.png)

#### cron表达式

秒 分钟 小时 日期 月份 星期 [年份]

cron表达式为6位或者7位，第7位为年份，为可选项。

#### 例外规则-Calendar

AnnualCalendar

CronCalendar

DailyCalendar

HolidayCalendar

MonthlyCalendar

WeeklyCalendars





Scheduler -> StdScheduler



quartz相关资料参考：

quartz表结构：

https://www.cnblogs.com/JiHC/p/13283583.html

表结构说明：

https://blog.csdn.net/weixin_42510888/article/details/113305480

Springboot整合Quartz集群部署以及配置Druid数据源：

https://blog.csdn.net/qq_39669058/article/details/90411497

Quartz 配置：

https://blog.csdn.net/tokyou/article/details/139019707



表结构查找方式：

![image-20240807231212753](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408072312713.png)



集群配置：

集群实例ID

集群开关

数据库持久化

数据源信息



集群部署需要解决的问题：

1.任务重跑

2.任务漏跑

保证集群的机器的时间是一致的。



simpleThreadPool 包工头

Work Thread 工人

quartzSchedulerThread 项目经理



getScheduler方法创建ThreadPool，创建调度器，创建调度线程。

调度线程初始处于暂停状态。

scheduler 将任务添加到jobStore

scheduler.start()方法激活调度器，QuartzSchedulerThread从timeTrigger取出待触发的任务，并包装成TriggerFiredBundle，然后由JobRunShellFactory创建TriggerFiredBundle的执行线程JobRunShell，调度执行通过线程池SimpleThreadPool去执行JobRunShell，而JobRunShell执行的就是任务类的execute方法：job.execute(JobExecutionContext context);



白话文描述：

1.调度器线程run()
2.获取等待下一个即将触发的trigger
2.1数据库的LockS表TRIGGER ACCESS加锁
2.2读取相应的jobDetail信息
2.3读取trigger表中触发器的信息并且标记为已经获取
2.4commit事务，释放锁



3.触发我的Trigger
3.1数据库的lockS表TRIGGER ACCESS加锁
3.2确认当前的Trigger状态
3.3读取Trigger的jobDetail信息
3.4 读取Trigger的Calendar信息
3.5更新Trigger信息
3.6commit事务 ，释放锁



4.实例化并执行job4.1从线程池获取线程并且执iobRunShell





## 任务调度之Elastic-Job

peer to peer 去中心化

leader节点只负责分片，不负责任务调度

elastic-job是一个轻量级定时任务调度框架，

官网：https://shardingsphere.apache.org/elasticjob/index_zh.html

