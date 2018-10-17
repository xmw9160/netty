package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.GroupMessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 群聊响应处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 13:54
 * @since V1.0
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket msg) {
        String message = "群聊[" + msg.getGroupId() + "]-" + msg.getFromUserName() + ": [" + msg.getMessage() + "].";
        System.out.println(message);
    }
}
