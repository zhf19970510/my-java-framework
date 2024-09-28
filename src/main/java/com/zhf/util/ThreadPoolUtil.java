package com.zhf.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务上做线程池隔离，可以保证业务有序执行
 */
@Component
public class ThreadPoolUtil {

    ConcurrentHashMap<Integer, ExecutorService> map = new ConcurrentHashMap<>();
    private Integer num;
    private Integer divide;
    private AtomicInteger divideIncr = new AtomicInteger();
    private AtomicInteger tempInt = new AtomicInteger();

    public ThreadPoolUtil(Integer numIp, Integer divideNum) {
        this.num = numIp;
        this.divide = divideNum;
        initMap();
    }

    public ThreadPoolUtil(Integer numIp) {
        this(numIp, 0);
    }

    private void initMap() {
        for (int i = 0; i < this.num; i++) {
            map.put(i, Executors.newSingleThreadExecutor());
        }
    }

    /**
     * 分段思想
     * 如果调用这个方法分key了，那么调用下一个方法  routeToThread 也是传入分段之后的key，即该方法的返回值
     * 可以应用于结合redis扣减库存的操作
     *
     * @param key key
     * @return 分段后的key
     */
    public String getKey(String key) {
        if (this.divide > 0) {
            return key + "_" + tempInt.incrementAndGet() % this.divide;
        }
        return key;
    }

    public Future<Boolean> routeToThread(String key, Callable<Boolean> call) {
        ExecutorService es = map.get(key.hashCode() % num);
        Future<Boolean> submit = es.submit(call);
        return submit;
    }
}
