package com.piccus.serper.client;

import com.piccus.serper.protocol.SerperResponseDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new HttpRequestEncoder());
        sc.pipeline().addLast(new HttpResponseDecoder());
        sc.pipeline().addLast(new SerperResponseDecoder());
        sc.pipeline().addLast(new SerperClientHandler());
    }
}
