package com.piccus.serper.protocol;

import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperRequestDecoder extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(SerperRequestDecoder.class);

    private StringBuilder sb;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            logger.debug("Server receive a request by requestId : " + request.headers().get("requestId"));
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
            SerperRequest request = Converter.stringToRequest(sb.toString());
            ctx.fireChannelRead(request);
        }
    }
}
