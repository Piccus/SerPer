package com.piccus.serper.Sample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("MyHandler channelReadComplete");
        super.channelReadComplete(ctx);
    }
}
