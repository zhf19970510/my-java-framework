## ElasticJob

如果想自定义实现ElasticJob，可以参考SimpleJob的实现：

![image-20240831084451068](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408310845569.png)



ServiceLoader SPI: 核心思想，java将装配的控制权转移到程序之外，而不在程序之内。

serviceLoader正常工作唯一要求是要有一个无参构造。不然没有办法在加载期间实例化。



job的初始化：

缓存配置、zookeeper、job config、scheduler、jobdetail、trigger、

lead选举监听

分片变化监听

失效转移监听

监视器设置变化监听

实例停止监听：停掉对应节点定时任务

trigger监听器 - 重新执行任务

Reschedule监听器 - cron发生配置变化或者路径变化，重新执行任务

担保监听 start节点发生改变 - 通知任务启动 或者 完成节点改变 - 通知任务完成

zookeeper连接状态监听 - 监控节点连接状态，控制定时任务暂停和重启

选举leader节点 - 失效转移或者本身的leader选举，这里用到了zookeeper对应的分布式锁机制。



#### job执行源码以及事件追踪

ElasticJobExecutor 仿造quartz进行设计 

execute()方法

获取job配置

jobFacade.checkJobExecutionEnvironment();

 -> configService.checkMaxTimeDiffSecondsTolerable();

  -> 最大容忍时间差，比对zookeeper写入时间与本地时间对比，是否大于最大容忍时间差，是一个漏跑的防范。

jobFacade.getShardingContexts();

 -> 获取shardingItems,将job配置和分片配置组合，将ShardingContext返回。

 -> 是否开启失效转移，开启失效转移，剔除失效节点，重新分片，从本节点返回分片，并将分片的结果写入到zookeeper中

链路事件监听（如果错过调度misFire，会让zookeeper干一件事情，让zookeeper取出调度错过的节点，然后继续去执行

ElasticJob有三种Job形态：Simple、Script、DataFlow

Script - 将分片参数转换为内部的命令行再进行调用

DataFlow 有两种模式：Stream、OneOff(只调用一次fetchData和processData)



#### 作业监听器

分为两类：常规监听器、分布式监听器。

删除作业监听器、实现常规监听器，实现ElasticJobListener

常规监听器和作业监听器需要通过SPI进行加入，才能被ElasticJobLite感知到。

![image-20240831203947880](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408312039859.png)

作业与yml配置：下面的配置就加上了监听器，监听器需要放在META-INF下面通过SPI识别到，还有对应的job要添加@Component注解，让spring识别到

```yaml
elasticjob:
	reg-center:
		server-lists: "192.168.189.1:2181"
		namespace: "my-job"
		connection-timeout-milliseconds: 100000
		max retries: 5
	jobs:
		MySimpleJob:
			elasticJobClass: com.zhf.elacticjob.job.MysimpleJob
			shardingTotalCount: 3
			cron: "0/5****?"
			description: "这个作业3个分片，每5秒钟执行一次"
			overwrite: true
		jobListenerTypes: MySimpleJobListener	# 根据getlype方法的返回值来匹配合适的作业
		shardingItemParameters: "0=北京,1=上海,2=深圳"
```



#### ElasticJob的作业分片的自定义分片

JobShardingStrategy

AverageAllocationJobShardingStrategy
描述:基于平均分配算法的分片策略，也是默认的分片策略。
具体规则:
如果分片不能整除，则不能整除的多余分片将依次追加到序号小的服务器。如:如果有3台服务器，分成9片，则每台服务器分到的分片是:1=[0,1,2],2=[3,4,5],3=[6,7,8].如果有3台服务器，分成8片，则每台服务器分到的分片是:1=[0,1,6],2=[2,3,7],3=[4,5].如果有3台服务器，分成10片，则每台服务器分到的分片是:1=[0,1,2,9],2=[3,4,5],3=[6,7,8]



OdevitySortByNameJobShardingStrategy
描述:根据作业名的哈希值奇偶数决定IP升降序算法的分片策略。
具体规则:
根据作业名的哈希值奇偶数决定IP升降序算法的分片策略。|作业名的哈希值为奇数则IP升序。|作业名的哈希值为偶数则IP降序。用于不同的作业平均分配负载至不同的服务器。



RotundRobinByNameJobShardingStrategy

作业名称的hash码的绝对值%作业服务器的数量

描述:根据作业名的哈希值对服务器列表进行轮转的分片策略。
具体规则:
根据作业名的哈希值奇偶数决定IP升降序算法的分片策略。|作业名的哈希值为奇数则IP升序。|作业名的哈希值为偶数则IP降序。用于不同的作业平均分配负载至不同的服务器。



自定义作业分片策略
具体规则:
Jobshardingstrategy接口并实现gharding方法，接口方法参数为作业服务器IP列表和分片策略选项，分片策略选项包括作业名称，分片总数以及分片序列号和个性化参数对照表，可以根据需求定制化自己的分片策略。

![image-20240831233457350](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202408312334277.png)

与SpringBoot集成的项目可以参考：

https://github.com/kuhn-he/elastic-job-lite-spring-boot-starter