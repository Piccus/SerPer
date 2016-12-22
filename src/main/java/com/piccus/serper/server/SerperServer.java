package com.piccus.serper.server;

import com.piccus.serper.core.SerperService;
import com.piccus.serper.protocol.SerperRequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperServer implements ApplicationContextAware, InitializingBean{

    private static Logger logger = Logger.getLogger(SerperServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Map<String, Object> beanMap = new HashMap<>();
    private ApplicationContext ctx;
    private String serverAddress;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(SerperService.class);
        serviceBeanMap.forEach((key, val) ->
                beanMap.put(val.getClass().getAnnotation(SerperService.class).value().getName(), val));
    }

    public SerperServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new HttpResponseEncoder());
                            sc.pipeline().addLast(new HttpRequestDecoder());
                            sc.pipeline().addLast(new SerperRequestDecoder());
                            sc.pipeline().addLast(new SerperServerHandler(beanMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture f = b.bind(host, port).sync();
            logger.debug("Server start on port : " + f.channel().localAddress());
        } catch (Exception e) {
            logger.error(e, e.getCause());
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public ApplicationContext getSpringApplicationContext() {
        return ctx;
    }

}