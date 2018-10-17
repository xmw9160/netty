package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.request.GroupMessageRequestPacket;
import com.xmw.wechat.protocol.response.GroupMessageResponsePacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 群聊消息处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 12:52
 * @since V1.0
 */
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket msg) {
        // 获取信息
        String groupId = msg.getGroupId();
        String sendMessage = msg.getMessage();
        // 获取ChannelGroup
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setFromUserId("0001");
            messageResponsePacket.setFromUserName("system");
            messageResponsePacket.setMessage("群聊不存在, 请确认后重试");
            ctx.channel().writeAndFlush(messageResponsePacket);
            return;
        }
        // 获取用户信息
        Session session = SessionUtil.getSession(ctx.channel());
        // 发送群聊消息
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getUserName());
        responsePacket.setMessage(sendMessage);
//        channelGroup.forEach(channel -> {
//            // 发送给非自己群员
//            if (!channel.equals(ctx.channel())) {
//                channel.writeAndFlush(responsePacket);
//            }
//        });
        channelGroup.writeAndFlush(responsePacket);
    }
}
