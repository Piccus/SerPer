package com.piccus.serper.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperServer {

    private static Logger logger = Logger.getLogger(SerperServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private volatile static SerperServer server;
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    private SerperServer(int port) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new SerperServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port == 0 ? 8000 : port).sync();
            logger.debug("Server start on port : " + f.channel().localAddress());
        } catch (Exception e) {
            logger.error(e, e.getCause());
        }
    }

    public static SerperServer getInstance() {
        return getInstance(0);
    }

    public static SerperServer getInstance(int port) {
        if (server == null) {
            synchronized (SerperServer.class) {
                if (server == null)
                    server = new SerperServer(port);
            }
        }
        return server;
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static void main(String[] args) {
        SerperServer.getInstance();
    }

}