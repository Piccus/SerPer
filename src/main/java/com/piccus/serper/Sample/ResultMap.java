package com.piccus.serper.Sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Piccus on 2016/12/14.
 */
public class ResultMap {
    private static Map<String, String> map = new ConcurrentHashMap<>();

    public static void add(String request, String result) {
        map.put(request, result);
    }

    public static String get(String request) {
        return map.get(request);
    }

    public static boolean finish(String request) {
        return map.containsKey(request);
    }
}
