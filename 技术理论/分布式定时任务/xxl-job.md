## XXL-JOB

#### 总体特性：

性能：更多、更快

可靠性：任务超时、任务失败、节点故障转移

运维：UI界面、日志、报表、告警、权限



#### xxl-job-admin配置

执行sql脚本

修改数据库密码

修改邮箱账号和授权码

添加配置：

```
xxl.job.login.username=admin
xxl.job.login.password=123456
```



执行器集群部署：

连接同一套数据库

集群时间需要保持一致，不然可能造成重复调度

可以集群之外提供一个nginx做负载均衡，对外只暴露一个域名



#### 执行器配置

添加依赖

```xml
<dependency>
	<groupId>com.xuxueli</groupId>
	<artifactId>xxl-job-core</artifactId>
	<version>${project.parent.version}</version>
</dependency>
```

修改配置

```
xxl.job.admin.addresses=http://127.0.0.1:7391/xxl-job-admin
xxl.job.accessToken=  # 配置动态用户生成的token，执行器和调度器两边保持一致，
xxl.job.executor.appname=xxl-job-executor-zhf  # 执行器名称，集群执行器名称需保持一致
### xxl-job executor registry-address: default use address to registry , otherwise use ip:port if address is null
# 解决动态ip、端口问题
xxl.job.executor.address=
### xxl-job executor server-info
xxl.job.executor.ip=
xxl.job.executor.port=9999
```

运行模式：

Bean - 在项目里写java类

GLUE - 在编辑器里直接写脚本代码

JobHandler：job添加了注解，对应属性值的value



任务分类：

普通任务 - 简单任务

分片任务

命令行任务

Http任务

生命周期任务 - 启动结束做点什么



XXL-JOB开发步骤：

1. 返回固定ReturnT<String>、参数固定String param 方法名自定义
2. 添加@XxlJob注解，属性(init, destroy)
3. 日志打印：XxlJobLogger.log



#### 分片任务

为什么要分片？？

就算使用多线程也是在同一台机器上操作，占用的是同一个机器的资源。

如何实现分片？？

只需要将路由策略改为分片广播即可。

调度器在给执行器去分发任务的时候给每一个执行器去分发不同的分片序号。

通过序号实现真正的分配。



#### 设计思想

调度与任务解耦

全异步化、轻量级

均衡调度



#### 原理

启动任务之后，任务是怎么跑完的，最终是怎么判断结果的



##### 执行器的启动与注册

![image-20240902000012763](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409020000150.png)

从执行器源码开始看：

XxlJobConfig -> XxlJobSpringExecutor -> super.start() 即 XxlJobExecutor.start

 start方法主要干了哪些事？

  创建连接调度器的客户端

  初始化日志清理线程

   Trigger回调线程

   this.initEmbedServer

​       -> 启动Netty服务 new ServerBootStrap(); 

​           this.startRegistry(appName, address); 

​              ->ExecutorRegistryThread.getInstance().start(appname, address);

​                       这是一个后台的守护线程，

​                       adminBiz.registry(registryParam);

​                          -> 通过http请求到调度器，调度器执行注册逻辑：

​                          xxlJobRegistryDao.registryUpdate



调度中心又是如何启动的？

XxlJobAdminConfig implements InitializingBean

 -> afterPropertiesSet

   -> xxlJobScheduler = new XxlJobScheduler();

   -> xxlJobScheduler.init();

​	  ![image-20240902223715989](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409022237506.png)

6. start-shedule 启动调度器

​     JobScheduleHelper.getInstance().start();

         1. 创建并启动调度线程
            2. 创建并启动时间轮线程



调度线程：（指派任务）

1. 先随机睡眠4~5秒，防止同时启动竞争
2. 预读取任务数，默认6000              // pre-read count : treadpool-size * trigger-qps
3. while死循环 只要调度线程不停止，就不断地调度
4. 本质还是通过select for update竞争获取锁资源，防止重跑，加上了行锁。
5. 如果加锁成功，执行调度流程，执行任务；如果加锁失败，关闭连接，进入下一次while循环，继续获取锁，直到获取锁资源不超时，才会进入到调度流程。
6. 会把最近满足条件要触发的任务查出来，查询5秒内需要触发的任务。
7. 根据取出来的任务划分为三种情况：

![image-20240902233507601](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409022335218.png)

第一种情况，任务过期5秒以上，放弃任务。

​	刷新对应任务的下次触发时间

第二种情况，已经到了触发时间，但是没超过5秒，正常触发。 

   trigger 触发任务

   JobTriggerPoolHelper.trigger()  触发任务

​       addTrigger();

​            // 调度器启动的时候是启动了两个线程池的，一个是快的线程池一个是慢的线程池，在上面代码截图中有体现。

​            // choose thread pool 根据情况选择线程池用来触发任务，如果jobTimeoutCount 大于一个指定时长，则选择慢线程池，这样设计的目的就是为了实现线程隔离，更好地利用线程资源。

​            // 拿到线程池后，取出线程执行调度请求，XxlJobTrigger.trigger

​                 // load data 根据任务ID从数据库拿到完整信息

​                 // 检查方法参数中是否存在赋值 failRetryCount ? 如果没有就从jobInfo获取。如果配置了本次调度失败，还可以再次进行调度。

​                // processTrigger()  开始真正地要触发一个任务了。

​					// 获取阻塞策略，分为三类策略，单机串行、丢弃后续调度、覆盖之前调度。

​                    // 获取路由策略

​                      // 如果需要分片，需要根据传入的分片参数，包括分片序号和总分片数进行分片

​                      // 执行器拿到分片参数之后，才能根据分片序号进行过滤数据

​                      // 由执行器远程执行任务 - runExecutor

​					     // 获取注册的业务实例，因为是客户端请求，走Client实现，Biz

​                         ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);

​                          runResult = executorBiz.run(triggerParam);  // 实际是调用了一个rest接口

​                        // 到这里调度器已经执行完毕了，返回执行器

   更新下次触发时间

   触发完了，再预读一次，看看下次触发时间与nowTime相比是否还在5秒之内。

      - 为了更加高效地利用资源，可以在一次调度里面去进行两次触发。为了避免有一些任务漏跑。
      - 计算时间轮位置，并放入时间轮、更新下次触发时间。
      - 这里面的代码和第三种情况是一模一样的。

第三种情况，还没到下次触发时间，早着呢

   计算时间轮位置，并放入时间轮、更新下次触发时间。



##### 时间轮是什么，放入时间轮怎么处理？

来了1000个任务，要将任务触发时间精确到秒级，怎么做？

1种简单的做法就是将这1000个任务装入一个集合，然后写一个while死循环不断地遍历这1000个任务，找出与当前时间匹配的任务进行执行

如果可以设计一种数据结构，将先执行的任务排在前面，做一个有序数组

其实jdk种就自带了这样的数据结构，Timer，Timer中有一个TaskQueue queue的成员变量，它是一个优先队列。它会按照你的规则把你需要先触发的任务排在前面。后台有一个线程不断地扫描这个队列，判断这个队列中的任务有没有到达触发时间，但是这个数据结构是但线程的，性能极其低下。

所以它有替代品，就是ScheduledThreadPoolExecutor，它的底层其实也是一个队列，DelayedWorkQueue 是一个延迟队列，也是优先队列，但是时间复杂度还是不算太好。

当数据量特别大，添加和删除任务很慢的时候，怎么进行优化？

如果很多任务在同一时间执行，可以采用数组+链表的方式实现，相同执行时间的任务放在同一数组下标，实现任务分组。

![image-20240903065952808](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409030659843.png)

但是如果时间跨度特别长，那这个数组特别长，还有什么优化手段吗？

如果把这个数组长度定义为60，每个格子代表1秒钟，

可以使用循环数组，取模思想。然后每个格子存的是一个双向链表。类似于一个时钟，最外层，也就是时间越靠后的任务，会往双向链表里面进行插入。时间轮插入删除一个任务的时间复杂度都是O(1).

![image-20240903070439907](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409030704213.png)

代码中体现时间轮代码：(放入时间轮的代码是在调度器这边的，真正执行任务是调度器会调用一个执行器rest接口，去真正执行任务)

```java
// 1、make ring second   计算时间轮位置
int ringSecond = (int)((jobInfo.getTriggerNextTime()/1000)%60);

// 2、push time ring     放入时间轮，从上面代码可知，调度任务是查询5s之内的任务，用60长度的循环数组足以存储一轮的任务，所以在xxl-job中对应的轮次只有第一轮
pushTimeRing(ringSecond, jobInfo.getId());

// 3、fresh next     更新下次触发时间
// 从下次触发时间开始算的，下次触发时间
refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
```

pushTimeRing方法的实现如下：

```java
private void pushTimeRing(int ringSecond, int jobId){
	// push async ring  拿到时间轮的任务链表
	List<Integer> ringItemData = ringData.get(ringSecond);
	if (ringItemData == null) {
		ringItemData = new ArrayList<Integer>();
		ringData.put(ringSecond, ringItemData);
	}
	ringItemData.add(jobId);

	logger.debug(">>>>>>>>>>> xxl-job, schedule push time-ring : " + ringSecond + " = " + Arrays.asList(ringItemData) );
}
```

数组+链表 = 散列表

执行器端会不断地使用一个线程去接收来自调度器端的请求：

```java
// invoke
bizThreadPool.execute(new Runnable() {
	@Override
	public void run() {
		// do invoke
		Object responseObj = process(httpMethod, uri, requestData, accessTokenReq);

		// to json
		String responseJson = GsonTool.toJson(responseObj);

		// write response
		writeResponse(ctx, keepAlive, responseJson);
	}
});
```

所以真正执行代码逻辑的地方在下面的代码中：

![image-20240903221053708](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032210198.png)

// push data to queue 最终会把执行的参数放入到队列中：
ReturnT<String> pushResult = jobThread.pushTriggerQueue(triggerParam);

LinkedBlockingQueue<TriggerParam> triggerQueue;

然后进入该JobThread对应的run方法查看逻辑：

它会首先判断是否设置了超时时间，如果有超时时间，不会直接进行执行，它会进入到一个FutureTask异步调用当中，进行异步地调用，最终会进入到finally代码块，执行interrupt方法。

![image-20240903223128723](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032231502.png)

如果没有设置超时时间，则直接走正常调用逻辑。

![image-20240903223251713](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032232150.png)

底层本质是通过反射进行调用的：

![image-20240903223411435](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032234085.png)

还有一个步骤，就是需要将执行的结果汇报给调度器，代码是在哪里进行实现的？

其实就在JobThread对应run方法最外层的finally代码块中进行实现的。

![image-20240903223807465](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032238043.png)

```java
// 可以看出这边也是采用队列使用异步调用的方式进行实现的，所以现在要看的就是TriggerCallbackThread对应的run()方法
public static void pushCallBack(HandleCallbackParam callback){
	getInstance().callBackQueue.add(callback);
	logger.debug(">>>>>>>>>>> xxl-job, push callback request, logId:{}", callback.getLogId());
}
```

查看该类没有继承于Thread，所以看对应的start方法。

![image-20240903224322298](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032243430.png)

最终调用了adminBiz的callback方法：此时执行器作为客户端，奖结果上报给调度器

![image-20240903224539299](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032245723.png)

![image-20240903224637577](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032246260.png)

服务端实现：

![image-20240903224801580](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032248620.png)

在上面截图方法中会调用一个callback方法，在该方法中会更改执行的结果更新到数据库中，在对应的callback方法最后可以看到对应实现。

![image-20240903225141223](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032251355.png)

整体调度流程：

![image-20240903225558352](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409032256526.png)

#### XXL-JOB二次开发参考

https://blog.csdn.net/m0_37527542/article/details/104468785

本地xxl-job源码存放路径：
D:\javastudy\schedule\xxl-job-2.2.0-src\xxl-job-2.2.0