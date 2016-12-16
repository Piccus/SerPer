package com.piccus.serper.Sample;

import com.piccus.serper.future.SerperFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.UUID;

/**
 * Created by Piccus on 2016/12/9.
 */
public class HttpClient {

    private static Logger logger = Logger.getLogger(HttpClient.class);

    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseDecoder());
                            ch.pipeline().addLast(new HttpRequestEncoder());
                            ch.pipeline().addLast("sample",new HttpClientIntHandler());
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();

            URI uri = new URI("http://127.0.0.1:8000");
            String msg = "com.piccus.serper.Sample.StaticSample.test";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes()));

            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set("messageType", "normal");
            request.headers().set("businessType", "testServerState");
            request.headers().set("RequestId", "001");
            HttpClientIntHandler handler = (HttpClientIntHandler)f.channel().pipeline().get("sample");
            SerperFuture serperFuture = handler.sendRequest(request);
            logger.info(serperFuture.get());
            logger.info(serperFuture.get());
            logger.info(serperFuture.get());
            logger.info(serperFuture.get());

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 8000);
    }
}
