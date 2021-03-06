package com.xmw.wechat.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xmw.wechat.protocol.common.Attributes;
import com.xmw.wechat.session.Session;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * 登录工具类
 *
 * @author mingwei.xia
 * @date 2018/10/10 14:47
 * @since V1.0
 */
public class SessionUtil {

    // userId -> channel 的映射
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    // groupId -> channelGroup
    private static final Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
//        return channel.hasAttr(Attributes.SESSION);
        return channel.attr(Attributes.SESSION) != null && channel.attr(Attributes.SESSION).get() != null;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }

    public static void addChannelGroup(String groupId, ChannelGroup channels) {
        channelGroupMap.put(groupId, channels);
    }

    public static void removeChannelGroup(String groupId) {
        if (channelGroupMap.get(groupId) != null) {
            channelGroupMap.remove(groupId);
        }
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return channelGroupMap.get(groupId);
    }


    /**
     * 标记客户端已登录
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 判断客户端是否已经登录
     */
//    public static boolean hasLogin(Channel channel) {
//        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
//        return loginAttr.get() != null;
//    }
}
