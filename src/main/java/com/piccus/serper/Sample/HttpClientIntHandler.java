package com.piccus.serper.Sample;

import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/9.
 */
public class HttpClientIntHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(HttpClientIntHandler.class);

    //private ByteBufToBytes reader;
    private StringBuilder sb;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            logger.info("CONTENT_TYPE : " + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
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
            ctx.close();
        }
    }
}
