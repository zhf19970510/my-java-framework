消息中间件三大功能:
异步、解耦、流量削峰

一个分区不能被多个消费者消费，但是一个消费者可以消费多个分区，
负载均衡策略是根据分区负载的，而不是根据消息负载的。

num.partitions指定默认的分区数量，默认为1个。

日志默认超过这个时间就会清理
log.retention.hours=168

日志检查的时间间隔
log.retention.check.interval.ms=300000

分区怎么划分？
如果要做到完全的顺序消息，一个分区

kafka一条消息的发送和消费的流程：

为什么kafka在客户端发送的时候需要做一个缓冲呢？
1. 减少IO的开销（单个 -> 批次）
2. 减少GC（池化技术---缓冲池）
缓冲发送受这两个参数限制：（只要一个满足）
batch.size
linger.ms
缓冲池：池的大小
buffer.memory

kafka的群组消费---负载均衡策略建立在分区级别（一个分区不能同时给一个消费者群组中的多个消费者消费）

当消费群组增加消费者后，会通过再均衡监听器（5秒轮询一次），进行负载再均衡。

kafka的消费全流程：
多线程安全问题：
    多线程安全：多线程去访问一个类，这个类始终表现出正确的行为.（不管运行的环境，让线程交替执行，不需要额外的同步、协同）
    生产者是多线程安全。确保多线程安全---线程封闭（每个线程，实例化一个consumer对象）
    消费者
群组协调：
    组协调器：（在broker)
        1）选举Leader消费者客户端
        2）处理申请加入组的客户端
        3）再平衡后同步新的分配方案
        4）心跳（与客户端维持）判断 哪些客户端 离线
        5）管理消费者已经消费的偏移量（_consumer_offset 默认50个）


    消费者协调器：（在consumer端）
        1）向组协调器 发起入组请求（第一个成为群主）
        2）发起同步组（消费者分区划分情况）的请求（leader客户端，负责计算，分配策略作为入参传入）
        3）心跳（与组协调器 维持）
        4）发起已经提交的消费者偏移量的请求（ACK确认）
        5）主动的发起离组请求

​    当一个消费者加入分组 （组协调器、消费者协调器 干的哪些事情）：
​    1. 消费客户端 启动、重连 （JoinGroup 请求 -> 组协调器）
​    2. 客户端已经完成JoinGroup，客户端（消费者协调器）再次向组协调器发起SyncGroup（同步组），获取新的分配方案
​    3. 客户端关机、异常，触发 离组
​    4. 客户端加入组之后（一直保持心跳）max.poll.interval.ms

分区再均衡：
    重写再均衡协调器，参考自己写的kafka-review中的HandlerRebalance，重写onPartitionsRevoked和onPartitionsAssigned，记录消息偏移量的位置

kafka整体架构文章：
https://www.cnblogs.com/zjxiang/p/15378355.html


kafka的集群与可靠性:
1. kafka做集群，目标：
    1. 高并发
    2. 高可用
    3. 动态扩容
复制：数据、分区都可以复制
kafka：天生分布式、集群、动态扩容

搭建kafka集群需要修改一些配置：
server.properties:
listeners=PLAINTEXT://xxx.xxx.xxx.xx:9092
broker.id=0     两台broker的brokerId不能相同，比如一台为0，一台为1
zookeeper.connect=xxx.xxx.xxx.xxx:2181

nohup ./start_ZK.sh -> zk.log &

主题的复制因子 --replication-factor的值不能超过节点个数，不然会报错，建议这个参数配置成和节点个数一致

创建节点:
./kafka-topics.sh --bootstrap-server 192.xxx.xxx.xxx:9092 --create --topic xxx --partitions 2 --replication-factor 2

kafka的集群衍生出来的概念：
1. 控制器
    本身就是一个broker节点，另外负责分区首领的选举


2. 首领副本：只有一个，针对的是每一个分区只有一个
    往分区中推送消息或者从分区中拿消息，只和首领副本有关

3. 跟随者副本：个数决定于创建主题时候的复制因子的值

列出所有主题的详细信息：
./kafka-topic.sh --bootstrap-server xxx.xxx.xxx.xxx:9092 --describe

消息生产的acks：
    =0,表示只要消息推送就不管了，就表示成功了
    =1,表示只要有一个副本写入了就确认
    =-1,表示所有副本写入才确认（min.insync.replicas参数）都收到了，就返回确认

消息获取的ISR：


总结一下：
1. 追求性能 min.insync.replicas=1 leader同步成功返回成功。（leader宕机了，必然会丢数据）
2. min.insync.replicas = 3，至少要有3个副本同步成功，3个副本的服务器，任意一台宕机（发送时）客户端发送不成功。


对于消费者来说，有几个比较重要的参数：
1. group.id
2. auto.offset.reset 有两个值：earliest,lasted
3. enable.auto.commit 默认开启自动提交
配置参数：auto.commit.interval

kafka的存储机制：
基本存储-分区
kafka的超时数据的清理机制：
    内部有一张hash表，每个元素包括键值的散列值和对应消息的偏移量，它们加起来刚好是24byte，哪怕是清理1G的数据，只需要24M的空间，这样可以更好更快地去清理

kafka相关配置参数详情：
https://kafka.apache.org/documentation.html#configuration

auto.leader.rebalance.enable    是否允许定期进行leader选举    false

Producer 以下几个核心：
1. 初始化
2. 发送流程
3. 缓冲区
4. IO处理

初始化-源码分析
1. 设置分区器
2、重试的参数：发送消息不一定成功，有重试机制，结合retries和重试间隔设置重试规则
3、序列化起
4、设置拦截器
5、有一些消息、发送的参数
6、Accumulator---累加器
7、初始化集群对应的元数据（topic,partition等等）
8、Sender(发送消息的线程)
9、启动IOThread,运行runnable


发送流程-源码分析：
1、执行拦截器的逻辑
2、获取集群配置信息
3、获取key和value的序列化器，序列化
4、分区器、获取要发送的分区，优先消息中的partition
5、写到缓冲区 后就结束了   Accumulator
    Deque 队列放入的元素是 ProducerBatch? 但是这和BufferPool有什么关系？
        在new Accumulator 的时候，new 了一个BufferPool 作为构造方法的传参
        分为Batches队列和Buffer Pool（内存池）
    内存池的大小默认是32M
    每个批次默认值是16KB
        缓冲区核心代码设计
        最终放数据的是：MemoryRecords中的 ByteBuffer
        ByteBuffer、BufferStream -> ByteBufferOutputStream、appendStream 这三者什么关系？
            BufferStream除了最基本的流的API功能之外，还提供自动扩容的能力，
            appendStream提供数据压缩能力
            1. bytebuffer不具备拓展，使用BufferStream提供自动扩容
            2. appendStream里面还可以对BufferStream增加压缩功能

所以看了源码之后，有什么帮助？如何优化生产？
1. acks参数调整
2. max.request.size 默认1M，如果推送的消息比较大，可以考虑调大该参数值
3. retries 重试参数 生产重试次数，默认0，不重试，建议改成2或3（参考RocketMQ），不建议改成太大，retryBackOffMs（默认是100，）和这个参数配合使用，
4. compression.type默认没有压缩，压缩消耗的是CPU,默认不走压缩
5. buffer.memory 缓冲区的大小，默认值32M，建议可以调大，调大的代价是占用客户端的内存。
6. batch.size 发送到缓冲区的消息 被封装为一个个batch的大小，默认是16KB，建议生产时可以适当调大。
7. linger.ms 看业务，业务允许延迟，可以调为不为0的值


在生产端IO的处理
    1. RecordAccumulator.append 发送消息跳转到缓冲的实现Accumulator
    2. Deque -> tryAppend   一条消息，进入一个queue(Deque)，进入tryAppend方法，加了sync锁）
    3. sender.wakeup()  RecordAccumulator 把消息交给sender线程进行消息的发送  消息发送的时机：达到阈值（batch.size / linger.ms）

NetworkClient.doSend


Consumer消费者源码：
主干：拉取消息进行消费，消费偏移量的提交

1. consumer初始化
    NetworkClient -> ConsumerNetworkClient
    ConsumerCoordinator : 消费者协调器
    Fetcher: 拉取消息组件
    消费者端核心参数：
        fetch.min.bytes，每次拉取消息的最小字节数，如果没有满足，则等，直到满足条件。 默认值是1,
        fetch.max.bytes，每次拉取消息的最大字节数，只能消费这个限制值以下的数据。如果有单个消息超过这个限制，一直会卡在这里，无限重试
        fetch.max.wait.ms 与 fetch.min.bytes配合，默认值500ms。如果一直没有满足最小拉取字节数，达到这个时间也会进行拉取
        max.partition.fetch.bytes，默认值是1M
        假如A主题下有20个分区，5个消费者。消费者内存计算与预估时：每个消费者最少要有多少内存来接收消息呢？ --- 4M
        max.poll.records 每次poll方法返回的最大记录数
2. 如何选举Consumer Leader
    1. 向Broker打个招呼
    2. 发起入组请求
    3. 选举leader（服务端做的，代码是scala语言写的，逻辑第一个就是Leader）
3. Consumer Leader是如何定制分区方案
    分区的策略：
        partition.assignment.strategy
            Range：平分，把连续的分区分配给消费者
            RoundRobin：分区循环分配给消费者
            Sticky：粘性分区：初始分区 和 RoundRobin一致，每一次分配变更，相对做最少的变动

   1. 制定分区方案
   2. 分组数据的分配
   3. 网络通信
4. Consumer如何拉取数据
    当前面的准备工作做好，就是去拉取对应分区的消息而已，逻辑不复杂
5. Consumer的自动偏移量提交
    默认是5s，，
    源码分析：ConsumerCoordinator 组件来进行提交
    每次poll到最后的代码处理，会自动提交处理（参数默认，或设置是自动提交）
    间隔是5s，如果没有到下次自动提交的间隔，也不会提交

message.max.bytes:表示一个服务器能够接收处理的最大字节数，注意这个值producer和consumer必须设置一致，且不要大于fetch.message.max.bytes属性的值
（消费者能读取的最大消息，这个值应该大于或等于message.max.bytes)。该值默认值是1000000字节，大概900KB~1MB。这个值的大小对性能影响很大，值越大，网络和IO
时间越长，还会增加磁盘写入的大小。

发送者通过sender发送消息：网络通信
在kafka生产者发送消息，主线程是先把消息交给RecordAccumulator，通过append方法进行追加的，
append方法里头有一个while(true)，accumulator可以理解为一个缓冲
追加消息线程的数量通过一个AtomicInteger appendsInProgress 变量进行计数
然后定义一个ByteBuffer buffer变量

重要的核心代码：
Deque<ProducerBatch> dq = topicInfo.batches.computeIfAbsent(effectivePartition, k -> new ArrayDeque());

![image-20240330204920415](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302049089.png)

这句代码中：topicInfo来自topicInfoMap

TopicInfo topicInfo = topicInfoMap.computeIfAbsent(topic, k -> new TopicInfo(logContext, k, batchSize));

topicInfoMap定义如下：

private final ConcurrentMap<String, /\*topic\*/,TopicInfo> topicInfoMap = new CopyOnWriteMap<>();

1)发送消息转跳到 缓冲的实现Accumulator

![image-20240330205609327](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302056991.png)

2)一条消息进入一个queue（Deque），进入tryAppend方法（加了sync锁）

![image-20240330205641543](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302056111.png)

sender线程发送消息：

![image-20240330212457472](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302125078.png)

Sender线程触发的时机是怎么样的？

3）消息发送的时机：达到阈值(batch.size, linger.ms)

![image-20240330213349100](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302133630.png)

对应下图：

![image-20240330213804412](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302138455.png)

因为Sender继承Runnable，所以主要看对应的run方法： 

![image-20240330214443425](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403302144310.png)

updateNodeLangecyStats(Integer nodeId, long nowMs, boolean canDrain): 把相关的数据放入nodeStats，修改节点状态

nodeStats是一个Map，它的定义如下：

private final ConcurrentMap<Integer, /\*nodeId\*/, NodeLangecyStats> nodeStats = new CopyOnWriteMap<>();

重头戏？

从accumulator拉去消息ProducerBatch

addToInFlightRequest():

![image-20240331054416842](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310544193.png)

封装消息，并封装请求，使用client发送消息

![](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310545988.png)

![image-20240331055910718](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310559985.png)

![image-20240331060347750](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310603282.png)

![image-20240331060444725](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310604873.png)

NIO三大核心组件：

Selector

Channel

buffer缓冲区

![image-20240331091944844](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403310919094.png)

kafka中的select模型：

![image-20240331100103933](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311001483.png)

NetworkClient.ready() -> selector.connect()

这个方法里面最终会初始化selector连接：

![image-20240331101418591](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311014174.png)

Selector参数有哪些？

nioSelector

Map<String, KafkaChannel> channels

Selector中比较重要的参数：

// Java NIO中用来监听网络I/O事件的Selector

private final java.nio.channels.Selector nioSelector;

// 通道 方便管理

private final Map<String, KafkaChannel> channels;

// 已经发送完成的send集合

private final List<NetworkSend> completedSends;

// 已经接收完成的send集合

private final LinkedHashMap<String, NetworkReceive> completedReceives;

// 立刻连接的key的集合

private final Set<SelectionKey> immediatelyConnectedKeys;

// 关闭连接的节点集合
private final Map<String, ChannelState> disconnected;

// 连接成功的节点集合

private final List<String> connected;

// 用来构造 KafkaChannel 的工具类

private final ChannelBuilder channelBuilder;

// 可以接收的最大数据量大小

private final int maxReceiveSize;

// 空闲超时到期的管理类

private final IdleExpiryManager idleExpiryManager;

// 用来管理byteBuffer的内存池

private final MemoryPool memoryPool;

//indicates if the previous call to poll was able to make progress in reading already-buffered data.
//this is used to prevent tight loops when memory is not available to read any more data
private boolean madeReadProgressLastPoll = true;

![image-20240331104150875](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311041824.png)

![image-20240331104814842](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311048830.png)

发送代码：发送数据的核心

![image-20240331111700387](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311117667.png)

![image-20240331112011765](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311120014.png)

selector中的poll方法才是真正地进行消息的发送，可以看到Sender的run方法，Sender是一个Runnable，run方法最后调用了Selector中的poll方法

Sender.run() -> runOnce() -> client.poll() -> selector.poll()

![image-20240331113231910](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311132710.png)

通过poll方法点进去，可以看到真正处理过消息发送和网络通信的是下面的方法方法：

![image-20240331142321134](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202403311423891.png)

上述方法核心就是对各种事件进行处理。

1. connect事件，TCP握手
2. Read 接收响应
3. write 发送消息
4. 这个方法最终会调用到write方法：channel.write()；才是真正的发送

![image-20240401194235709](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404011942249.png)