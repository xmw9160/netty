package com.xmw.wechat.server.handler;

import java.util.ArrayList;
import java.util.List;

import com.xmw.wechat.protocol.request.ListGroupMembersRequestPacket;
import com.xmw.wechat.protocol.response.ListGroupMembersResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 会员信息查看逻辑处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:57
 * @since V1.0
 */
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket msg) {
        String groupId = msg.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        List<Session> sessionList = new ArrayList<>();
        if (channelGroup != null) {
            channelGroup.forEach(channel -> sessionList.add(SessionUtil.getSession(channel)));
        }

        // 构建响应
        ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessionList);
        responsePacket.setMessage("success");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
