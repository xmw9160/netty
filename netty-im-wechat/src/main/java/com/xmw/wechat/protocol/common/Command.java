package com.xmw.wechat.protocol.common;

/**
 * Command 指令常量类
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:57
 * @since V1.0
 */
public interface Command {
    /**
     * 登录请求
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录响应
     */
    Byte LOGIN_RESPONSE = 2;

    /**
     * 消息请求
     */
    Byte MESSAGE_REQUEST = 3;

    /**
     * 消息响应
     */
    Byte MESSAGE_RESPONSE = 4;
}
