package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.JoinGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 加入群聊响应逻辑
 *
 * @author mingwei.xia
 * @date 2018/10/17 9:54
 * @since V1.0
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println("加入群聊[" + msg.getGroupId() + "]成功!");
        } else {
            System.err.println("加入群聊[" + msg.getGroupId() + "]失败，原因为：" + msg.getMessage());
        }
    }
}
