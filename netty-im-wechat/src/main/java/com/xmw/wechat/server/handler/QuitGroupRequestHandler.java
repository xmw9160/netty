package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.request.QuitGroupRequestPacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import com.xmw.wechat.protocol.response.QuitGroupResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 退出群聊逻辑处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:21
 * @since V1.0
 */
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket msg) {
        //1.获取群对应的 channelGroup，然后将当前用户的 channel 移除
        String groupId = msg.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        boolean quitSuccess = channelGroup != null;
        if (quitSuccess) {
            // 退出群聊
            channelGroup.remove(ctx.channel());
            // 退出群聊通知
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            Session session = SessionUtil.getSession(ctx.channel());
            messageResponsePacket.setFromUserId(session.getUserId());
            messageResponsePacket.setFromUserName(session.getUserName());
            String message = session.getUserName() + ", 退出了群聊[" + groupId + "]";
            if (channelGroup.size() > 1) {
                messageResponsePacket.setMessage(message);
            } else {
                messageResponsePacket.setMessage(message + ", 群聊只剩您一个人, 群聊解散...");
            }
            channelGroup.forEach(channel -> channel.writeAndFlush(messageResponsePacket));
            // 群聊只有一个人时逻辑处理, 移除群聊
            if (channelGroup.size() == 1) {
                SessionUtil.removeChannelGroup(groupId);
            }
        }
        //2.构造退群响应发送给客户端
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();
        responsePacket.setSuccess(quitSuccess);
        responsePacket.setGroupId(groupId);
        responsePacket.setMessage(quitSuccess ? "退出群聊成功" : "群聊不存在");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
