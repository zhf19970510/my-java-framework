package com.zhf.job.simple_elastic_job;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration;

import javax.sql.DataSource;

// 添加component 就与spring boot集成了
//@Component
public class SimpleJobTest {
    // TODO 如果修改了代码，跑之前清空ZK
    public static void main(String[] args) {
        new ScheduleJobBootstrap(createRegistryCenter(), new MySimpleJob(), createJobConfiguration())
                .schedule();
    }

    // 注册中心
    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("192.168.101.5:2181", "zhf-job");
        zc.setConnectionTimeoutMilliseconds(100000);
        zc.setMaxRetries(5);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zc);
        regCenter.init();
        return regCenter;
    }

    private static DataSource getDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setURL("jdbc:mysql://127.0.0.1:3306/test01?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false");
        return dataSource;
    }

    // 作业配置
    private static JobConfiguration createJobConfiguration() {
        TracingConfiguration tracingConfiguration = new TracingConfiguration("RDB", getDataSource());
        String jobs = "0=看论文,1=写代码,2=搞事情,3=搞大事情,4=谈恋爱,5=写博客,6=出版书籍";
        return JobConfiguration.newBuilder("MySimpleJob", 7)
                .cron("0/5 * * * * ?")
                .shardingItemParameters(jobs)
                .addExtraConfigurations(tracingConfiguration)
                // 使用自定义的作业分片策略
//                .jobShardingStrategyType("Shuffle")
                //允许客户端配置覆盖注册中心
                .overwrite(true)
                //故障转移
                .failover(true)
                .build();
    }


}
