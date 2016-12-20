package com.piccus.serper.protocol;

import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperResponseDecoder extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(SerperResponseDecoder.class);

    private StringBuilder sb;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Client receive a response.");
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            logger.debug("Client receive a response by requestId : " + response.headers().get("requestId"));
            sb = new StringBuilder();
        }

        if (msg instanceof HttpContent && !(msg instanceof LastHttpContent)) {
            HttpContent httpContent =  (HttpContent) msg;
            ByteBuf content = httpContent.content();
            sb.append(Converter.byteBufToString(content));
            content.release();
        }

        if (msg instanceof LastHttpContent) {
            HttpContent httpContent =  (HttpContent) msg;
            ByteBuf content = httpContent.content();
            sb.append(Converter.byteBufToString(content));
            SerperResponse response = Converter.stringToResponse(sb.toString());
            ctx.fireChannelRead(response);
        }
    }
}
