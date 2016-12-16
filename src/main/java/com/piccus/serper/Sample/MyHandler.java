package com.piccus.serper.Sample;

import com.piccus.serper.core.SerperInvoker;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/9.
 */
public class MyHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(MyHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("MyHandler channelActive.");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("MyHandler channelRead : " + msg);
        if (msg instanceof String) {
            String source = (String) msg;
            String resultStr = (String) SerperInvoker.invoke(source);
            logger.info("Invoker return : " + resultStr);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(resultStr.getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            response.headers().set("RequestId", "001");
            ctx.writeAndFlush(response);
        }

        //super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("MyHandler channelReadComplete");
        super.channelReadComplete(ctx);
    }
}
