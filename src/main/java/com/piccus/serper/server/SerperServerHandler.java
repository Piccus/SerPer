package com.piccus.serper.server;

import com.piccus.serper.core.SerperInvoker;
import com.piccus.serper.protocol.SerperRequest;
import com.piccus.serper.protocol.SerperResponse;
import com.piccus.serper.tools.Converter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Piccus on 2016/12/19.
 */
public class SerperServerHandler extends SimpleChannelInboundHandler<SerperRequest> {

    private static Logger logger = Logger.getLogger(SerperServerHandler.class);

    private Channel channel;

    private final Map<String, Object> beanMap;

    public SerperServerHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SerperRequest request) throws Exception {
        logger.debug("Server get a request.");
        SerperServer.submit(() -> {
            logger.info("Starting thread for making response");
            SerperResponse response = new SerperResponse();
            response.setRequestId(request.getRequestId());
            try {
                Object result = invoke(request);
                response.setResult(result);
            } catch (Throwable t) {
                response.setError(t.toString());
                logger.error("Server comes a error during invoking : ", t);
            }
            sendResponse(response);
        });
    }

    private Object invoke(SerperRequest request) throws Throwable{
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        Object bean = beanMap.get(className);
        Class<?> clazz = bean.getClass();
        Method method = SerperInvoker.getMethod(clazz, methodName, parameterTypes);
        return SerperInvoker.invokeMethod(method, bean, parameters);
    }

    private void sendResponse(SerperResponse response) {
        String str = Converter.toJsonString(response);
        ByteBuf buf = Converter.stringToByteBuf(str);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, buf);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        httpResponse.headers().set("requestId", response.getRequestId());
        channel.writeAndFlush(httpResponse);
        logger.debug("Server send a response, requestId : " + response.getRequestId());
    }
}
