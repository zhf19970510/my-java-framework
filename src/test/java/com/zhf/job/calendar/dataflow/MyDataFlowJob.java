package com.zhf.job.calendar.dataflow;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;

import java.util.Arrays;
import java.util.List;


public class MyDataFlowJob implements DataflowJob<String> {
    public List<String> fetchData(ShardingContext shardingContext) {
        System.out.println("开始获取数据");
        // 假装从文件或者数据库中获取到了数据
//        Random random = new Random();
//     if( random.nextInt() % 3 != 0 ){
//            return null;
//        }
        return Arrays.asList("严老师","马老师","晁老师");
    }

    public void processData(ShardingContext shardingContext, List<String> data) {
        for(String val : data){
            // 处理完数据要移除掉，不然就会一直跑
            System.out.println("开始处理数据："+val);
        }

    }
}
