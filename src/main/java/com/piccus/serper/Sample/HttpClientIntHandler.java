package com.piccus.serper.Sample;

import com.piccus.serper.tools.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/9.
 */
public class HttpClientIntHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(HttpClientIntHandler.class);

    private ByteBufToBytes reader;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            logger.info("CONTENT_TYPE : " + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
            if (HttpHeaders.isContentLengthSet(response)) {
                    reader = new ByteBufToBytes((int) HttpHeaders.getContentLength(response));
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();

            if (reader.isEnd()) {
                String resultStr = new String(reader.readFull());
                logger.info("Server said : " + resultStr);

                ctx.close();
            }
        }
    }
}
