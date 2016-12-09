package com.piccus.serper.net;

import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Piccus on 2016/12/6.
 */
public class StringEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String in, ByteBuf out) throws Exception {
        out.writeBytes(Converter.stringToByteBuf(in));
        ctx.flush();
    }
}
