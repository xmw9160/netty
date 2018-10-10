package com.xmw.wechat.util;

import com.xmw.wechat.protocol.common.Attributes;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * 登录工具类
 *
 * @author mingwei.xia
 * @date 2018/10/10 14:47
 * @since V1.0
 */
public class LoginUtil {
    /**
     * 标记客户端已登录
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 判断客户端是否已经登录
     */
    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }
}
