package com.piccus.serper.client;

import com.piccus.serper.Sample.IStaticSample;
import com.piccus.serper.core.SerperInvoker;
import com.piccus.serper.future.Sync;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperClient implements ApplicationContextAware, InitializingBean{

    private static final Logger logger = Logger.getLogger(SerperClient.class);

    private volatile static SerperClient client;

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private SerperClientHandler handler;

    private String serverAddress;

    private ApplicationContext applicationContext;

    //private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
    //       TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));

    public SerperClient(String serverAddress) {
        this.serverAddress = serverAddress;

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new SerperClientInitializer());

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture f = b.connect(host, port).sync();
            handler = f.channel().pipeline().get(SerperClientHandler.class);
            handler = f.channel().pipeline().get(SerperClientHandler.class);
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess())
                    logger.debug("Successfully connect to server : " + f.channel().remoteAddress());
            });
        } catch (Exception e) {
            logger.error(e, e.getCause());
        }
    }

    private SerperClient() {
       // threadPoolExecutor.submit(() -> {
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup).channel(NioSocketChannel.class)
                        .handler(new SerperClientInitializer());

                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture f = b.connect(host, port).sync();
                handler = f.channel().pipeline().get(SerperClientHandler.class);
                handler = f.channel().pipeline().get(SerperClientHandler.class);
                f.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess())
                        logger.debug("Successfully connect to server : " + f.channel().remoteAddress());
                });
            } catch (Exception e) {
                logger.error(e, e.getCause());
            }
        //});
    }


   public static SerperClient getInstance() {
        return client;
    }

    public void stop() {
        //threadPoolExecutor.shutdown();
        workerGroup.shutdownGracefully();
    }

    public SerperClientHandler getHandler() {
        return handler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = applicationContext.getBean(SerperClient.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
