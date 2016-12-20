package com.piccus.serper.client;

import com.piccus.serper.future.SerperFuture;
import com.piccus.serper.protocol.SerperRequest;
import com.piccus.serper.protocol.SerperResponse;
import com.piccus.serper.tools.Converter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperClientHandler extends SimpleChannelInboundHandler<SerperResponse>{

    private static Logger logger = Logger.getLogger(SerperClientHandler.class);

    private volatile Channel channel;
    private ConcurrentHashMap<String, SerperFuture> map = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SerperResponse response) throws Exception {
        SerperFuture future = map.get(response.getRequestId());
        map.remove(response.getRequestId());
        future.done(response);
        logger.debug("Client get a response.");
    }

    public SerperFuture sendRequest(SerperRequest request) throws Exception{
        URI uri = new URI("http://127.0.0.1:8000");
        String msg = Converter.toJsonString(request);
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Converter.stringToByteBuf(msg));
        httpRequest.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
        httpRequest.headers().set("requestId", request.getRequestId());
        channel.writeAndFlush(httpRequest);
        logger.debug("Client send a request, requestId : " + request.getRequestId());
        SerperFuture future = new SerperFuture(request);
        map.put(future.getRequestId(), future);
        return future;
    }
}
