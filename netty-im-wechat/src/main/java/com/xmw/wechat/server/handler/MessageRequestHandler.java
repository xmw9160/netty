package com.xmw.wechat.server.handler;

import java.util.Date;

import com.xmw.wechat.protocol.request.MessageRequestPacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * MessageRequestHandler
 *
 * @author mingwei.xia
 * @date 2018/10/11 9:52
 * @since V1.0
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) {
        System.out.println(new Date() + " : 收到客户端信息: " + msg.getMessage());
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMessage("服务端回复【" + msg.getMessage() + "】");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
