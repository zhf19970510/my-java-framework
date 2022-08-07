package com.zhf.sync;

import java.util.concurrent.*;

/**
 * 自定义异步 Future类
 *
 * @param <T>
 */
public class SyncFuture<T> implements Future<T> {

    private CountDownLatch latch = new CountDownLatch(1);

    private T response;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return null != response;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        latch.wait();
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return response;
        } else {
            return null;
        }
    }

    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }
}
