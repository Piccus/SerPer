package com.piccus.serper.Sample;

import com.piccus.serper.tools.ByteBufToBytes;
import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/7.
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter{

    private static Logger logger = Logger.getLogger(HttpServerInboundHandler.class);

    //private ByteBufToBytes reader;

    private StringBuilder sb;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("HttpServerInboundHandler channelRead Called!");
        if (msg instanceof HttpRequest) {
            logger.info("HttpServerInboundHandler channel read a HttpRequest.");
            HttpRequest request = (HttpRequest) msg;
            logger.info("messageType: " + request.headers().get("messageType"));
            logger.info("businessType: " + request.headers().get("businessType"));
            int length = (int) HttpHeaders.getContentLength(request);
            logger.info("HttpContent Length: " + length);
            sb = new StringBuilder();
            //if (HttpHeaders.isContentLengthSet(request)) {
            //    reader = new ByteBufToBytes(length);
            //}
        }

        if (msg instanceof HttpContent && !(msg instanceof LastHttpContent)){
            logger.info("HttpServerInboundHandler channel read a HttpContent.");
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            sb.append(Converter.byteBufToString(content));
            content.release();
        }

        if (msg instanceof LastHttpContent) {
            logger.info("HttpServerInboundHandler channel read a LastHttpRequest.");
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            sb.append(Converter.byteBufToString(content));
            //reader.reading(content);
            content.release();

            /*if (reader.isEnd()) {
                String resultStr = new String(reader.readFull());
                logger.info("Client said: " + resultStr);

                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer("I am ok".getBytes()));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);

            }*/

            logger.info("Client said: " + sb.toString());

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer("I am ok".getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("HttpServerInboundHandler channelReadComplete");
        ctx.flush();
    }
}
