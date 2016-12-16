package com.piccus.serper.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Pack200;

/**
 * Created by Piccus on 2016/12/16.
 */
public class SerperFuture implements Future<Object> {

    private String resultStr;

    private String requestId;

    private Sync sync;

    private ReentrantLock lock = new ReentrantLock();

    public SerperFuture(String requestId) {
        sync = new Sync();
        this.requestId = requestId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (resultStr != null)
            return resultStr;
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (resultStr != null)
                return resultStr;
            else
                return null;
        } else {
            throw new RuntimeException("Timeout exception. Request ID : " + requestId);
        }
    }

    public void done(String resultStr) {
        this.resultStr = resultStr;
        sync.release(1);
    }

    public String getRequestId() {
        return requestId;
    }
}
