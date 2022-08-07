package com.zhf.executor;

import com.zhf.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Service
public class LoadExecutor {
    public static final Logger logger = LoggerFactory.getLogger(LoadExecutor.class);

    private final ConcurrentMap<String, ExecutorService> executorsConcurrentMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        executorsConcurrentMap.put("sync1", Executors.newSingleThreadExecutor());
    }

    public <T> Future<T> submit(String key, Callable<T> task) {
        if (!executorsConcurrentMap.containsKey(key)) {
            logger.error("executor no key : {}", key);
            throw new ServiceException("key error");
        }
        return executorsConcurrentMap.get(key).submit(task);
    }
}
