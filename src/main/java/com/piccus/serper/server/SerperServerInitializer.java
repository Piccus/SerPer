package com.piccus.serper.server;

import com.piccus.serper.protocol.SerperRequestDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new HttpResponseEncoder());
        sc.pipeline().addLast(new HttpRequestDecoder());
        sc.pipeline().addLast(new SerperRequestDecoder());
        sc.pipeline().addLast(new SerperServerHandler());
    }
}
