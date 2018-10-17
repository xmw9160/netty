package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.request.JoinGroupRequestPacket;
import com.xmw.wechat.protocol.response.JoinGroupResponsePacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 进入群聊逻辑处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 9:26
 * @since V1.0
 */
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) {
        //1.获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
        String groupId = msg.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        boolean isSuccess = channelGroup != null;
        if (isSuccess) {
            // 给群聊成员发送消息
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            Session session = SessionUtil.getSession(ctx.channel());
            messageResponsePacket.setFromUserId(session.getUserId());
            messageResponsePacket.setFromUserName(session.getUserName());
            messageResponsePacket.setMessage(session.getUserName() + ", 加入群聊[" + groupId + "]");
            channelGroup.forEach(channel -> channel.writeAndFlush(messageResponsePacket));
            // 将申请者加入群聊
            channelGroup.add(ctx.channel());
        }
        //2.构造加群响应发送给客户端
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setSuccess(isSuccess);
        responsePacket.setGroupId(groupId);
        responsePacket.setMessage(isSuccess ? "加入群聊成功" : "群聊不存在");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
