package com.xmw.wechat.client.handler;

import java.util.Date;

import com.xmw.wechat.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * MessageResponseHandler 消息响应处理
 *
 * @author mingwei.xia
 * @date 2018/10/11 10:38
 * @since V1.0
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) {
        System.out.println(new Date() + " : 收到服务端消息: " + msg.getMessage());
    }
}
