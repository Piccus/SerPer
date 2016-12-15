package com.piccus.serper.core;

import com.piccus.serper.Sample.IStaticSample;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Piccus on 2016/12/14.
 */
public class SerperProxy<T> implements InvocationHandler{

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
        Class<?> cal = SerperInvoker.getClazz(packagePath + "." + className);
        T t = (T) SerperInvoker.getInstance(cal);
        Method mt = SerperInvoker.getMethod(cal, method.getName());
        return SerperInvoker.invokeMethod(mt, t);
    }

    public static void main(String[] args) {
        IStaticSample staticSample;
        staticSample = SerperInvoker.getProxyInstance(IStaticSample.class);
        System.out.print(staticSample.test());
    }
}
