LFU 、跳表、压缩表、

压缩表向上支撑其他三种数据结构：Sorted Set、Hash、List。

可以在编码的时候利用redis的压缩表利用bit位的思想实现算法思路。



编码的过程：

 尽量地去转int进行优化，数值使用int比str省太多的空间了。否则也要根据数据的len长度，使用不同类型的空间存数据。

解码的过程：

字节步进



hash数据结构：

zipList 到hash表的转换。

尽量控制条件，不发生转换：

转变边界通过变量控制：

hash-max-ziplist-entries 512

hash-max-ziplist-value 64



Set中底层为什么除了用Hash还会用IntSet结构？

还是为了做空间优化，可以更加节省空间，趋向于紧凑结构，数据结构紧凑，数据类型紧凑。

查看存储在redis中底层的编码类型：

object encoding s1



List：

1. 空间不连续；

2. lindex 时间复杂度：O(n)

   可以使用zipList进行优化，一个zipList存储若干个节点，zipList是存储了对应entry的长度，索引元素的时候先检查zipList的长度，如果大于该长度，则直接跳过该zipList，采用了分治的思想。

list底层用到了quickList。quickList底层的struct元素其实就是zipList。

空间利用率问题和分治颗粒度问题怎么解决？

quickList对应结构体中有一个int fill字段做平衡

fill值分为两种情况：

-1~-5：4096-65536bytes

> 0 : sz < 8192bytes && fill个entry



zset:

zipList

skipList

跳表包含存储数据的部分以及一个dict字典，加快检索效率

skipList底层用一个update数组存储指针指向位置以及一个变量存储累加的span值，加快了插入的效率，它内部使用span变量存储节点对应跨度值，加快了排名区间检索效率。



#### 持久化

RDB

AOF

bgsave和bgrewrite

propagate:

aof

replication

主线程的分配->I/O线程的读写->命令执行



AOF:

AOF文件怎么一点点加进去的，

AOF追加文件的同时，启一个子进程进行重写，同时还不能影响主线程，而且重写的东西里面还包含全量不能丢失的命令。

AOF中写的是指令，



源码中对于buffer文件的填充先回放入到一个全局server.aof_buf中。



在启动server过程中，会开启一个回调方法，本质其实是一个定时任务，在这个定时任务serverCron中会做以下的事情：

每次循环：

1. 客户端的处理；
2. db的过期key，rehash;
3. bgrdb；   对应命令为：bgsave
4. bgwriteaof;       对应命令为bgrewriteaof
5. 前台刷写aof；

整体执行命令流程：

读—>写db->写aof—>写客户端。



现在开始正式研究下底层源码流程：

bg是啥？

bg的时候，子进程如何取主进程增量的cmd

aofCreatePipes() 进程交互？

文件描述符、I/O、Pipe

yum install man man-pages

man 2 pipe

redis中bgrewrite过程中，会初始化这些值，都和pipe有关的，先暂记一下。

![image-20240915211043224](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409152110292.png)

pipe capacity:65535Bytes

blocking

noblock

epoll也会处理pipe

lsof -p 7182

![image-20240915212847485](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409152128534.png)

所以子进程和父进程是通过管道进行通信的。

进程间通过此种方式进行通信。所以创建好pipe是为了方便后面父进程和子进程进行通信。

然后redis会fork出一个子进程，

man 2 fork

fork底层实现使用了copy on write的思想。

fork子进程过程示意图：redis根据虚拟地址找到pagetables，cpu通过mmu找到物理内存。fork子进程的时候，物理内存还是共享的。因为fork完成之后没有执行execve，所以子进程还是属于redis的。

![image-20240915230624971](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409152306758.png)

但还是有一个老问题，子进程怎么知道父进程的数据变化的，这就利用到了上面所说的pipe管道了。

原来读写数据不仅可以通过I/O文件流、网络，进程之间还可以通过管道pipe进行通信，redis里面就用到了这种思想。你还记得bgaofrewrite的时候不是会fork出一个子进程嘛，子进程就是从这里通过管道读取主进程新增的数据

![image-20240916092046021](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409160920018.png)

往下就是主进程自己去append aof，然后rename。



#### 主从复制

弱一致性 异步 主从同步

全量同步 增量同步

全量同步下：

rdb -> file -> socket ->  从

![image-20240916111033931](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409161110728.png)

"rdb" -> socket 

![image-20240916112116929](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409161121290.png)

那同步发出的rdb文件是子进程发出的还是父进程发出的？

是父进程，父进程首先要和从节点保持socket连接，此时可能还没有fork出一个子进程出来。而且子进程主要干的事就是拿到父进程当时节点的数据，形成快照。

但是问题来了，父进程怎么拿到子进程当时快照节点的数据给从节点的？换句话说父进程怎么把某一时间节点的数据发给从节点中去的?

通过pipe，管道传输。

增量：
全量之后的增量，buf空间积攒。

断开之后重连的增量： 享受了buf。  max_memory < 10GB

多个slaves连接master获取增量数据，使用的是一个环形buff。

那么有那么多的slaves，一个环形buff看起来不够，因为不同的slave同步数据的快慢可能不一样的，所以还需要一个offset存储在slave端，记录增量数据的偏移位。这是一个1MB的环形buf，如果觉得太小，可以从两个方面考虑，是否需要开启主从复制？是否需要增加环形buf大小。

wirite：1.buf；2.注册写事件；3.等 eventloop

增量同步的实现细节：

通过socket还是file，单独有线程执行？BIO/NIO?



master和salve之间怎么知道是该进行全量同步还是增量同步的？

首先，salve会先给master发送psync请求，master在接收到请求之后，会确定该进行全量还是增量，并将响应结果返回给slave。

后面就是发生状态为已连接，并且开始推送数据接收数据的过程了。

从节点进入到全量同步模式后，会从NIO模式进入到BIO，全身心地进行同步这件事上来。



主从同步过程中 master一侧使用的是nio，slave使用的是bio。

master对应的父进程fork出的子进程推送数据到pipi管道，父进程-master通过eventloop读取子进程的数据发送给slave。加载RDB文件，在EventLoop中穿插着发送数据给slave。



为什么不让子进程 持有socket 往slave同步指令呢？可以做到吗？

可以做到，但是每一个slave都会对应一个子进程，如果这些socket不在一个网卡对应循环体内执行，势必会造成抢占网卡资源导致部分slave可能一直无法同步数据的情况。所以放在主进程上同步。而且这样的话还可能会影响主进程，进而影响client端响应。



redis中为什么会涉及到那么多Handler的切换？





阻塞、发布订阅、事务、lua、redission、redlock、双写一致性。





#### 阻塞

客户端发送阻塞指令时，redis不会停住。

![image-20240917153647754](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409171536255.png)

如果遇到阻塞命令，先将对应的阻塞的key和客户端存储到blocking_keys中，主线程继续执行其他事情，不会被阻塞住。一旦有一个客户端往队列中放入该key对应的数据，则相应地也会放入ready_keys，然后再unBlockClients。



#### 发布订阅

pubsub_channels

看不到历史数据的，和redisdb是一个级别。

基于发布订阅实现事件通知：（事件驱动）

![image-20240917164712572](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409171647426.png)

![image-20240917164805335](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409171648345.png)

redis内部事件channel机制默认是关闭的。

两种维度，一种是监控操作维度，一种是监控key维度。

![image-20240917165248261](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409171652955.png)

开启方式：

![image-20240917165422629](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409171654321.png)

set k1 uuid ex 10 nx;



#### 事务

懒事务<-原子（无回滚）保证exec执行完（有可能保存），要么就是不执行。

redis怎么实现事务的？

为客户端维护一个队列，命令本身就是排队。

事务：multi command exec

watch: 可以根据观察key对应的值是否执行指令。但是可能有不同的客户端观察不同的key，所以redis server在设计redisDb的时候有一个watched_keys的字典。如果watch监控到有别的客户端修改了对应的key值，则标记为dirty，该事物不执行。

在cmd处理的时候，分别对用的事件去处理：

1.ready_keys

2.__keys

3.watched_keys

redis是单线程



#### 项目引用redis

##### 性能

##### 双写一致性

理论的东西，有很多的tradeoff

这是一个演进的过程：

spring data redis -> redission

redission源码（异步/NIO/响应式/设计模式）



使用redis如何优化？

能不能让请求进入redis返回快一些，或者让redis的操作精简一些

可以利用异常机制，快速跳过后面的逻辑代码，参考my-java-framework项目的RedisJsonUtil.decreaseOptimise方法



使用缓存优化？

多级缓存，加本地缓存，是一个全局手段

单体服务可以加本地缓存



利用线程池隔离的思想进行优化？

参考：my-java-framework项目的ThreadPoolUtil类



加入缓存、加入线程池隔离、分治思想



jemeter线程组配置：

![image-20240917211424515](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409172114047.png)





#### redis缓存

##### cache-misses

冷启动

击穿

穿透

雪崩

##### cache-aside 缓存旁路

先读redis，如果没有，再读db，并更新redis。

普通write，即做set修改操作：双写一致性怎么保证？

先更新redis，根据redis的更新结果，去更新数据库（cache-write-through） 直写

热加载（cache-load)

如果发生数据不一致的情况，需要进行回更。这就是 （cache-write-behind）。



解决双写一致性方案：（异步场景）

往mq同时写入剩余量和需求量。剩余量也是通过redis扣减之后算出来的。加上一个version版本号的概念，传入数据到mq的时候还带上一个version版本号，回更的时候只针对一个version版本值进行操作，其他相同version值的被丢弃。这样就不会重复消费了。回更的时候version++。需要使用redis的hash结构存储，涉及多值。为了保证使用redis的正确性，可以加入事务，同时执行多步操作。但是事务有性能损耗。可以在事务的外层包上一层pipeline-管道。

![image-20240927060322111](C:/Users/admin/AppData/Roaming/Typora/typora-user-images/image-20240927060322111.png)





三高系统

高并发：

- 动静分离
- 多级缓存
- 负载均衡
- 接入层
- HTTP 3.0
- 响应式网关
- 常态、瞬时并发

高性能

- 性能指标

- 高并发下谈高性能

- 多级缓存、读写分离

- 异步设计

- 再论并发、莫谈一致性

- 不浪费，才是高性能
- 接口做好业务划分和切割

高可靠

- 再论负载均衡
- 服务无状态
- 服务治理
- 存储层可靠性
- 异地多活，可靠，性能，并发
- 业务隔离



AKF:

![image-20240928111354898](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202409281113961.png)



#### 学习技巧

I/O模型+线程模型

技术关联点之间的条件反射。



