package com.piccus.serper.client;

import com.piccus.serper.future.Sync;

/**
 * Created by Piccus on 2016/12/19.
 */
public class ConnectionManager {

    private SerperClient client;
    private Sync sync;

    public ConnectionManager(String host, int port) {
        sync = new Sync();
        client = SerperClient.getInstance(sync, host, port);
        sync.acquire(-1);
    }

    public ConnectionManager() {
        this(null, 0);
    }

}
