package com.piccus.serper.core;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Piccus on 2016/12/12.
 */
public class SerperInvoker {

    private static Logger logger = Logger.getLogger(SerperInvoker.class);

    public static Object invoke(String source) {
        int pos = source.lastIndexOf(".");
        String packgePath = source.substring(0, pos);
        String methodName = source.substring(pos + 1, source.length());
        int pos2 = packgePath.lastIndexOf(".");
        String className = packgePath.substring(pos2 + 1, packgePath.length());
        return invoke(packgePath, className, methodName);
    }

    public static Object invoke(String packgaName, String className, String methodName, Object... args) {
        Class<?> clazz = getClazz(packgaName);
        Method method = getMethod(clazz, methodName);
        Object instance = getInstance(clazz);
        return invokeMethod(method, instance,args);
    }

    public static Class<?> getClazz(String pacageName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(pacageName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return clazz;
    }

    public static Object getInstance(Class<?> clazz) {
        Object object= null;
        try {
            object = clazz.newInstance();
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        }
        return object;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        }
        return method;
    }

    public static Object invokeMethod(Method method, Object object, Object... args) {
        Object value = null;
        try {
            value = method.invoke(object, args);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
        }
        return value;
    }

}
