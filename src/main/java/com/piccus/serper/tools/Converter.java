package com.piccus.serper.tools;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Piccus on 2016/12/6.
 */
public class Converter {

    public static String toJsonString(Object o) {
        String str = JSON.toJSONString(o);
        return str;
    }

    public static String byteBufToString(ByteBuf byteBuf) {
        byte[] bytes= new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return new String(bytes);
    }

    public static ByteBuf stringToByteBuf(String str) {
        byte[] bytes = str.getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        return byteBuf;
    }
}
