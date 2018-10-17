package com.xmw.wechat.protocol.common;

import com.xmw.wechat.session.Session;

import io.netty.util.AttributeKey;

/**
 * 通用属性定义
 *
 * @author mingwei.xia
 * @date 2018/10/10 14:43
 * @since V1.0
 */
public interface Attributes {

    /**
     * 登录标识
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    /**
     * session
     */
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
