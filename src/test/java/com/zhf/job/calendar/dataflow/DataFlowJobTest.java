package com.zhf.job.calendar.dataflow;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;

public class DataFlowJobTest {
    // 如果修改了代码，跑之前清空ZK
    public static void main(String[] args) {
                new ScheduleJobBootstrap(createRegistryCenter(), new MyDataFlowJob(), createJobConfiguration())
                .schedule();
    }

    // 注册中心
    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("192.168.101.9:2181", "DataFlow");
        zc.setConnectionTimeoutMilliseconds(40000);
        zc.setMaxRetries(5);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zc);
        regCenter.init();
        return regCenter;
    }

    // 作业配置
    private static JobConfiguration createJobConfiguration() {
//        String jobs = "0=看论文,1=写代码,2=搞事情,3=搞大事情,4=谈恋爱,5=写博客,6=出版书籍";
        return JobConfiguration.newBuilder("MyDataFlowJob", 2)
                //分片数要比实际的运行节点要稍微的大一点
                .cron("0/5 * * * * ?")
//                .shardingItemParameters(jobs)
                // 使用自定义的作业分片策略
//                .jobShardingStrategyType("Shuffle")
//                .shardingItemParameters("0=RDP, 1=CORE, 2=SIMS, 3=ECIF")
                //允许客户端配置覆盖注册中心
                .overwrite(true)
                //故障转移
                .failover(true)
                .build();
    }

}
