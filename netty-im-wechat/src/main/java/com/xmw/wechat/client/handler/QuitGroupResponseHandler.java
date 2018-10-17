package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.QuitGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出群聊响应处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:17
 * @since V1.0
 */
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println("退出群聊[" + msg.getGroupId() + "]成功!");
        } else {
            System.err.println("退出群聊[" + msg.getGroupId() + "]失败，原因为：" + msg.getMessage());
        }
    }
}
