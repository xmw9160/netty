package com.xmw.wechat.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端连接数统计工具类
 *
 * @author mingwei.xia
 * @date 2018/10/11 16:28
 * @since V1.0
 */
public class ClientCountUtil {
    /**
     * 客户端连接数. 默认为0
     */
    private static final AtomicLong CLIENT_COUNT = new AtomicLong(0);

    /**
     * 新增客户端
     */
    public static void addClient() {
        CLIENT_COUNT.incrementAndGet();
    }

    /**
     * 减少客户端
     */
    public static void subClient() {
        CLIENT_COUNT.decrementAndGet();
    }

    public static void printClientInfo() {
        System.out.println("当前客户端连接数为: " + CLIENT_COUNT.get() + "个.");
    }
}
