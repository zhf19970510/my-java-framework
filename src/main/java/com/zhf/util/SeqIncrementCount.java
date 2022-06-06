package com.zhf.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 曾鸿发
 * @create: 2022-06-05 21:54
 * @description：
 **/
public class SeqIncrementCount {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private SeqIncrementCount(){

    }

    private static class InnerSqlCount{
        private static SeqIncrementCount seqCount = new SeqIncrementCount();
    }

    public static SeqIncrementCount getInstance(){
        return InnerSqlCount.seqCount;
    }

    public Integer getNextSeqNum(){
        return threadNumber.get() + 1;
    }

}
