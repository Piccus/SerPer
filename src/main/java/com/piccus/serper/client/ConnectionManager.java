package com.piccus.serper.client;

import com.piccus.serper.future.Sync;

/**
 * Created by Piccus on 2016/12/19.
 */
public class ConnectionManager {

    private SerperClient client;
    private Sync sync;

    public ConnectionManager() {
        sync = new Sync();
        client = SerperClient.getInstance(sync);
        sync.acquire(-1);
    }

}
