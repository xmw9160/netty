package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.ListGroupMembersResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 群聊群员信息响应处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 11:23
 * @since V1.0
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket msg) {
        System.out.println("群聊: [" + msg.getGroupId() + "]中的人包括：" + msg.getSessionList());
    }
}
