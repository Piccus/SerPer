package com.piccus.serper.tools;

import com.alibaba.fastjson.JSON;
import com.piccus.serper.protocol.SerperRequest;
import com.piccus.serper.protocol.SerperResponse;
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

    public static SerperResponse stringToResponse(String str) {
        SerperResponse response = JSON.parseObject(str, SerperResponse.class);
        return response;
    }

    public static SerperRequest stringToRequest(String str) {
        SerperRequest request = JSON.parseObject(str, SerperRequest.class);
        return request;
    }
}
