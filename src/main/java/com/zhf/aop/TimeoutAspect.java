package com.zhf.aop;

import com.zhf.annotation.Timeout;
import com.zhf.entity.base.BaseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: 曾鸿发
 * @create: 2022-04-15 14:49
 * @description：
 **/
@Aspect
@Component
public class TimeoutAspect {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutAspect.class);

    @Resource(name = "applicationTaskExecutor")
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    @Pointcut(value = "@annotation(com.zhf.annotation.Timeout)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object timeOut(ProceedingJoinPoint joinPoint) throws Exception {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        logger.info(method.getName() + " timeOut Aspect coming...");
        // Timeout timeout = method.getDeclaredAnnotation(Timeout.class);

        // AtomicReference<Thread> thread = new AtomicReference<>();

        Callable callable = () -> {
            logger.info(method.getName() + "正在执行timeout限制方法...");
            try {
                // thread.set(Thread.currentThread());
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                logger.error(method.getName() + " timeOut Aspect失败！", throwable);
                return BaseResult.error(" timeOut Aspect失败");
            }
        };

        System.out.println(Thread.currentThread().getId()); // 51

        Future<Object> future = applicationTaskExecutor.submit(callable);

        long start = System.currentTimeMillis();

        logger.info("优先方案已经开始执行");

        try {
            return future.get(2, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.info("共耗费 " + (System.currentTimeMillis() - start) + "毫秒还没有获取到结果！");
            future.cancel(true);
            return BaseResult.error("timeOut Aspect 超时");
        }


        // long t = System.currentTimeMillis();
        // long timeOut;
        // int i = 0;
        // while (true) {
        //     Thread.sleep(500);
        //     logger.info(method.getName() + "第" + (++i) + "次继续执行"  + " timeOut Aspec");
        //     timeOut = System.currentTimeMillis() - t;
        //     if (timeOut > timeout.value()) {
        //         logger.error(method.getName() + " timeOut Aspect 超时，耗费：" + timeOut + "毫秒");
        //         Thread.sleep(10);
        //         future.cancel(true);
        //         return BaseResult.error( "timeOut Aspect 超时");
        //     }
        //     if(future.isDone()){
        //         logger.info(method.getName() + " timeOut Aspect执行成功！");
        //         return future.get();
        //     }
        // }

    }
}
