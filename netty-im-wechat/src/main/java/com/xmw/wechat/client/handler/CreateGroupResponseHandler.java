package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.CreategroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 创建
 *
 * @author mingwei.xia
 * @date 2018/10/16 14:08
 * @since V1.0
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreategroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreategroupResponsePacket msg) {
        System.out.print("群创建成功，groupId 为[" + msg.getGroupId() + "], ");
        System.out.println("群里面有：" + msg.getUserNameList());
    }
}
