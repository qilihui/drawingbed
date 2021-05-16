package com.github.qilihui.drawingbed.util;

/**
 * @author qilihui
 * @date 2021/5/16 20:07
 */
public class ThreadLocalUtil {
    private final static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void addIp(String ip) {
        threadLocal.set(ip);
    }

    public static String getIp() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}
