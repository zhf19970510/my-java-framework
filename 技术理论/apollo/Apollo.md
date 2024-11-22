## Apollo

### 配置作用

可以在不重新编译代码的情况下，改变程序运行逻辑、调整边界值、被调用模块路由信息等等，用来方便维护，提高工作效率的一种手段。

核心概念：
application(应用)

environment(环境)

cluster(集群)

namespace(命名空间)： 私有类型、公有类型、关联类型。

![image-20241007204734568](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202410072047442.png)

将apollo-adminservice-2.0.1-github.zip、apollo-configservice-2.0.1-github.zip、apollo-portal-2.0.1-github.zip项目赋值到linux目录下，然后解压，修改对应项目的config目录下application.xxx的数据库连接信息

修改portal下对应的apollo-env.properties文件，即修改eureka注册中心服务地址，保留dev环境即可。

```properties
#local.meta=http://localhost:8080
dev.meta=http://localhost:8080
#fat.meta=http://fill-in-fat-meta-server:8080
#uat.meta=http://fill-in-uat-meta-server:8080
#lpt.meta=${lpt_meta}
#pro.meta=http://fill-in-pro-meta-server:808
```

启动apollo。

```
cd /zhf/work/soft/configservice/
nohup java -jar apollo-configservice-2.0.1.jar > configservice.log &
```

apollo默认的账号密码：apollo/admin.

如果发现启动的时候连接不上注册中心地址，可能是将公网转换为了私网，可以在启动命令中加入如下参数，即可解决问题：

-Denv=DEV  -Dapollo.config-service=http://106.14.138.180:8080



\# 可以在apollo的portal页面添加秘钥，开启秘钥，如果项目中没有配置秘钥，项目会一次重试连接，以2的幂次方进行重试，，但是依然可以获取到apollo配置，不过获取到的是本地缓存。
\#apollo.access-key.secret=fefaewfjiaejfioajiojioa



但是配置秘钥后依然获取不到，可能是因为服务器和本地时间不一致导致的。可以在服务器安装如下：

yum -y install ntp ntpdate

然后进行时间的同步：
ntpdate cn.pool.ntp.org



灰度发布有相应的规则的。规则对应的是ip地址。

灰度发布之后可以选择全量发布或者放弃灰度发布。



apollo的portal页面还可以配置集群，然后在项目中就需要配置apollo的集群信息：

\#apollo.cluster=jd

如果配置了多个命名空间，则多个命名空间以英文逗号分割。

#apollo.bootstrap.namespaces=application,bj



监听配置变更：通过api监听配置变更。



Apollo架构：

架构图：

![image-20241013112533486](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202410131125785.png)



为什么使用Eureka：
它提供了完整的Service Registry和Service Discovery实现。（引入jar包即可，不需要启动单独的服务）

和Spring Cloud无缝集成

Open Source。



服务端发送ReleaseMessage的实现方式：

![image-20241014201131062](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202410142011568.png)



Apollo采用的通知+拉取的方式获取配置信息。拉取回来的是有变化的key，真正获取数据的时候，还需要再获取一次请求。

客户端设计获取配置：
![image-20241014203842053](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202410142038559.png)

因为客户端具有本地缓存可用性很强。



源码可以从自动装配的代码处看起。



Apollo设置超时时间为90s，服务端设置返回时间为60s，如果有配置更新，立即返回；如果没有配置更新，到了60s也会返回。



Apollo源码课程流程图：

https://cloud.fynote.com/share/d/JlxHQVy3