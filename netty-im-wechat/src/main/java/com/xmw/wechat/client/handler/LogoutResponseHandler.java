package com.xmw.wechat.client.handler;

import com.xmw.wechat.protocol.response.LogoutResponsePacket;
import com.xmw.wechat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        // 解除session绑定
        SessionUtil.unBindSession(ctx.channel());
        if (logoutResponsePacket.isSuccess()) {
            // 1. 先打印
            System.out.println("成功退成登录....");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 2. 后打印
        System.out.println("channel 已关闭!!!");
    }
}
