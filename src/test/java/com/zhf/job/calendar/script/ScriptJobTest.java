package com.zhf.job.calendar.script;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;

public class ScriptJobTest {
    // 如果修改了代码，跑之前清空ZK
    public static void main(String[] args) {

        new ScheduleJobBootstrap(createRegistryCenter(), "SCRIPT", createJobConfiguration())
                .schedule();
    }

    // 注册中心
    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("192.168.189.1:2181", "ScriptJobs");
        zc.setConnectionTimeoutMilliseconds(100000);
        zc.setMaxRetries(5);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zc);
        regCenter.init();
        return regCenter;
    }

    // 作业配置
    private static JobConfiguration createJobConfiguration() {
//        String jobs = "0=看论文,1=写代码,2=搞事情,3=搞大事情,4=谈恋爱,5=写博客,6=出版书籍";

        return JobConfiguration.newBuilder("ScriptJobs", 2)
                .cron("0/4 * * * * ?")
                .description("脚本作业")
                .setProperty("script.command.line","D:\\1.bat")
                .overwrite(true)
                //故障转移
                .failover(true)
                .build();
    }

}
