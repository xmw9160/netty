package com.xmw.wechat.server.handler.optimize;

import com.xmw.wechat.protocol.request.MessageRequestPacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * MessageRequestHandler
 *
 * @author mingwei.xia
 * @date 2018/10/11 9:52
 * @since V1.0
 */
@SuppressWarnings("Duplicates")
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) {
//        System.out.println(new Date() + " : 收到客户端信息: " + msg.getMessage());
//        MessageResponsePacket responsePacket = new MessageResponsePacket();
//        responsePacket.setMessage("服务端回复【" + msg.getMessage() + "】");
//        ctx.channel().writeAndFlush(responsePacket);

        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());
        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getUserName());
        // 3.拿到消息接收方的 channel
        Channel channel = SessionUtil.getChannel(msg.getToUserId());
        // 4.将消息发送给消息接收方
        if (channel != null) {
            responsePacket.setMessage(msg.getMessage());
            channel.writeAndFlush(responsePacket);
        } else {
            // 接收方不在线, 提示用户
            System.err.println("[" + session.getUserId() + "] 不在线，发送失败!");
            responsePacket.setMessage(msg.getToUserId() + "对应的用户不存在");
            ctx.channel().writeAndFlush(responsePacket);
        }
    }
}
