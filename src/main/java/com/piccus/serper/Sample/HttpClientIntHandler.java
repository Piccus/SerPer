package com.piccus.serper.Sample;

import com.piccus.serper.future.SerperFuture;
import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Piccus on 2016/12/9.
 */
public class HttpClientIntHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(HttpClientIntHandler.class);

    private ConcurrentHashMap<String, SerperFuture> resultMap = new ConcurrentHashMap<>();

    //private ByteBufToBytes reader;
    private StringBuilder sb;

    private volatile Channel channel;

    private StringBuilder rqsb;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            rqsb = new StringBuilder();
            HttpResponse response = (HttpResponse) msg;
            logger.info("CONTENT_TYPE : " + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
            rqsb.append(response.headers().get("RequestId"));
            /*if (HttpHeaders.isContentLengthSet(response)) {
                    reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(response));
            }*/
            sb = new StringBuilder();
        }

        if (msg instanceof HttpContent && !(msg instanceof LastHttpContent)) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            sb.append(Converter.byteBufToString(content));
            content.release();
        }

        if (msg instanceof LastHttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            //reader.reading(content);
            sb.append(Converter.byteBufToString(content));
            content.release();
            /*if (reader.isEnd()) {
                String resultStr = new String(reader.readFull());
                logger.info("Server said : " + resultStr);

                ctx.close();
            }*/
            logger.info("Server said : " + sb.toString());
            SerperFuture future = resultMap.get(rqsb.toString());
            resultMap.remove(rqsb.toString());
            future.done(sb.toString());
            logger.info(future);
            ResultMap.add(rqsb.toString(), sb.toString());
        }
    }

    public SerperFuture sendRequest(DefaultFullHttpRequest request) {
        channel.writeAndFlush(request);
        SerperFuture serperFuture = new SerperFuture(request.headers().get("RequestId"));
        resultMap.put(serperFuture.getRequestId(), serperFuture);
        return serperFuture;
    }
}
