package com.zhf.util;

import com.zhf.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author: 曾鸿发
 * @create: 2022-03-02 13:25
 * @description：
 **/
@Slf4j
public class MyCollectionUtil {

    /**
     * 将list拆分
     * @param list
     * @param len
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> genericSplitList(List<T> list,  int len){
        if(CollectionUtils.isEmpty(list) || len < 1) {
            return new ArrayList<>();
        }
        List<List<T>> result = new ArrayList<>();

        int size = list.size();
        int count = (size + len - 1) / len;
        for(int i = 0; i < count; i++){
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    /**
     * 批量插入
     * @param list
     * @param insertFunction
     * @param batchAmount
     * @param <T>
     * @return
     */
    public static <T> int genericBatchInsert(List<T> list, Function<List<T>, Integer> insertFunction, int batchAmount){
        int total = 0;
        if(CollectionUtils.isEmpty(list)){
            return total;
        }
        List<List<T>> subLists = genericSplitList(list, batchAmount);
        for (List<T> subList : subLists) {
            total += insertFunction.apply(subList);
        }
        return total;
    }

    /**
     * list数据分页
     * @param list
     * @param start
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<T> pageList(List<T> list, int start, int size){
        int beginIndex = (start - 1) * size;
        if(beginIndex < 0) beginIndex = 0;
        int toIndex = Math.min(start * size, list.size());
        if(beginIndex > toIndex){
            log.error(String.format("Error begin index %d and toIndex %d", beginIndex, toIndex));
            throw new ServiceException("begin index should be less than to index");
        }
        return list.subList(beginIndex, toIndex);
    }

    /**
     * 此方法不好，需要显示地创建和销毁线程池
     * @param list
     * @param insertFunction
     * @param nThreads
     * @param <T>
     * @return
     */
    public static <T> Integer genericBatchInsertSync(List<T> list, Function<List<T>, Integer> insertFunction, int nThreads){
        int size = list.size();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for(int i = 0; i <= nThreads; i++){
            List<T> subList = list.subList(size / nThreads * i, Math.min(size / nThreads * (i + 1), size));
            if(CollectionUtils.isEmpty(subList)){
                break;
            }
            Callable<Integer> task1 = () -> insertFunction.apply(subList);
            executorService.submit(task1);
        }
        executorService.shutdown();
        return list.size();
    }

    public static <T> int genericBatchInsertAsync(List<T> list, Function<List<T>, Integer> insertFunction, int batchAmount, ThreadPoolTaskExecutor applicationTaskExecutor){
        int total = 0;
        if(CollectionUtils.isEmpty(list)){
            return total;
        }
        List<List<T>> subLists = genericSplitList(list, batchAmount);
        for (List<T> subList : subLists) {
            CompletableFuture.runAsync(() -> {
                insertFunction.apply(subList);
            }, applicationTaskExecutor);
        }
        return list.size();
    }

}
