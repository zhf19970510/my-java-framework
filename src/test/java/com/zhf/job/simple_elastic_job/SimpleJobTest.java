package com.zhf.job.simple_elastic_job;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;


public class SimpleJobTest {
    // TODO 如果修改了代码，跑之前清空ZK
    public static void main(String[] args) {
        new ScheduleJobBootstrap(createRegistryCenter(), new MySimpleJob(), createJobConfiguration())
                .schedule();
    }

    // 注册中心
    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("192.168.101.9:2181", "zhf-job");
        zc.setConnectionTimeoutMilliseconds(100000);
        zc.setMaxRetries(5);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zc);
        regCenter.init();
        return regCenter;
    }

    // 作业配置
    private static JobConfiguration createJobConfiguration() {
        String jobs = "0=看论文,1=写代码,2=搞事情,3=搞大事情,4=谈恋爱,5=写博客,6=出版书籍";
        return JobConfiguration.newBuilder("MySimpleJob", 7)
                .cron("0/5 * * * * ?")
                .shardingItemParameters(jobs)
                // 使用自定义的作业分片策略
//                .jobShardingStrategyType("Shuffle")
                //允许客户端配置覆盖注册中心
                .overwrite(true)
                //故障转移
                .failover(true)
                .build();
    }


}
