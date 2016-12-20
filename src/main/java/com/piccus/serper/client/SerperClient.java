package com.piccus.serper.client;

import com.piccus.serper.Sample.IStaticSample;
import com.piccus.serper.core.SerperInvoker;
import com.piccus.serper.future.Sync;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperClient {

    private static final Logger logger = Logger.getLogger(SerperClient.class);

    private volatile static SerperClient client;

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private SerperClientHandler handler;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    private SerperClient(Sync sync) {
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup).channel(NioSocketChannel.class)
                            .handler(new SerperClientInitializer());

                    ChannelFuture f = b.connect("127.0.0.1", 8000).sync();
                    handler = f.channel().pipeline().get(SerperClientHandler.class);
                    if (sync != null)
                        sync.release(1);
                    f.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess())
                                logger.debug("Successfully connect to server.");
                        }
                    });
                } catch (Exception e) {
                    logger.error(e, e.getCause());
                }
            }
        });
    }

    public static SerperClient getInstance() {
        if (client == null) {
            synchronized (SerperClient.class) {
                if (client == null)
                    client = new SerperClient(null);
            }
        }

        return client;
    }

    public static SerperClient getInstance(Sync sync) {
        if (client == null) {
            synchronized (SerperClient.class) {
                if (client == null)
                    client = new SerperClient(sync);
            }
        }

        return client;
    }

    public void stop() {
        workerGroup.shutdownGracefully();
    }

    public SerperClientHandler getHandler() {
        return handler;
    }

    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager();
        IStaticSample staticSample = SerperInvoker.getProxyInstance(IStaticSample.class);
        logger.info(staticSample.test());
    }

}
