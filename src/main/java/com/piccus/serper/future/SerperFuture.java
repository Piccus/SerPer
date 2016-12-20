package com.piccus.serper.future;

import com.piccus.serper.protocol.SerperRequest;
import com.piccus.serper.protocol.SerperResponse;

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

    private SerperRequest request;

    private SerperResponse response;

    private Sync sync;

    private ReentrantLock lock = new ReentrantLock();

    public SerperFuture(SerperRequest request) {
        this.request = request;
        sync = new Sync();
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
        if (response != null)
            return response.getResult();
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (response != null)
                return response.getResult();
            else
                return null;
        } else {
            throw new RuntimeException("Timeout exception. Request ID : " + request.getRequestId());
        }
    }

    public void done(SerperResponse response) {
        this.response = response;
        sync.release(1);
    }

    public String getRequestId() {
        return request.getRequestId();
    }
}
