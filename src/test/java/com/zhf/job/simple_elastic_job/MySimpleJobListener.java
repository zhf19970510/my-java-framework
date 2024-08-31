package com.zhf.job.simple_elastic_job;

import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;

import java.util.Date;

/**
 * 需要在META-INF下加入类的全限定名，文件名为：org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener
 * 文件内容为：com.zhf.job.simple_elastic_job.MySimpleJobListener
 */
public class MySimpleJobListener implements ElasticJobListener {

    public MySimpleJobListener() {
    }

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        System.out.println(new Date() + "准备数据的备份环境");
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        System.out.println(new Date() + "清理数据的备份环境");
    }

    @Override
    public String getType() {
        return "MySimpleJobListener";
    }
}
