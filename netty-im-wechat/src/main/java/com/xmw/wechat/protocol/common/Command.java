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

    /**
     * 登出请求
     */
    Byte LOGOUT_REQUEST = 5;

    /**
     * 登出响应
     */
    Byte LOGOUT_RESPONSE = 6;

    /**
     * 创建群聊请求
     */
    Byte CREATE_GROUP_REQUEST = 7;

    /**
     * 创建群聊响应
     */
    Byte CREATE_GROUP_RESPONSE = 8;

    /**
     * 加入群聊请求
     */
    Byte JOIN_GROUP_REQUEST = 11;

    /**
     * 加入群聊响应
     */
    Byte JOIN_GROUP_RESPONSE = 12;

    /**
     * 退出群聊请求
     */
    Byte QUIT_GROUP_REQUEST = 13;

    /**
     * 退出群聊响应
     */
    Byte QUIT_GROUP_RESPONSE = 14;

    /**
     * 群聊成员信息查看请求
     */
    Byte LIST_GROUP_MEMBER_REQUEST = 15;

    /**
     * 群聊成员信息查看响应
     */
    Byte LIST_GROUP_MEMBER_RESPONSE = 16;

    /**
     * 群聊消息请求
     */
    Byte GROUP_MESSAGE_REQUEST = 17;

    /**
     * 群聊消息响应
     */
    Byte GROUP_MESSAGE_RESPONSE = 18;
}
