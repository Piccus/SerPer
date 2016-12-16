package com.piccus.serper.future;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by Piccus on 2016/12/15.
 */
public class Sync extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 123L;

    private final int done = 1;
    private final int pending = 0;

    @Override
    protected boolean tryAcquire(int acquires) {
        return getState() == done;
    }

    @Override
    protected boolean tryRelease(int releases) {
        if (getState() == pending)
            if (compareAndSetState(pending, done))
                return true;
        return false;
    }

    public boolean isDone() {
        return getState() == done;
    }
}
