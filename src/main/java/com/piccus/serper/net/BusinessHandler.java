package com.piccus.serper.net;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/6.
 */
public class BusinessHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(BusinessHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String) msg;
        Person p = JSON.parseObject(str, Person.class);
        logger.info("BusinessHandler read msg from client : " + p.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
