package com.piccus.serper.core;

import com.piccus.serper.client.SerperClient;
import com.piccus.serper.future.SerperFuture;
import com.piccus.serper.protocol.SerperRequest;
import com.piccus.serper.tools.Converter;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Piccus on 2016/12/14.
 */
public class SerperProxy<T> implements InvocationHandler{

    private static final Logger logger = Logger.getLogger(SerperProxy.class);

    private Class<T> clazz;

    public SerperProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int pos = clazz.getName().lastIndexOf(".");
        String className = clazz.getName().substring(pos + 2, clazz.getName().length());
        String packagePath = clazz.getName().substring(0 ,pos);
        //Class<?> cal = SerperInvoker.getClazz(packagePath + "." + className);
        //T t = (T) SerperInvoker.getInstance(cal);
        //Method mt = SerperInvoker.getMethod(cal, method.getName());
        logger.debug("Proxy started invoker.");
        SerperRequest request = new SerperRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(packagePath + "." + className);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        logger.debug(Converter.toJsonString(request));
        SerperClient client = SerperClient.getInstance();
        SerperFuture future = client.getHandler().sendRequest(request);
        logger.info(Converter.toJsonString(request));
        return future.get();
    }
}
