package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * LoginRequestHandler
 *
 * @author mingwei.xia
 * @date 2018/10/11 9:51
 * @since V1.0
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(msg.getVersion());
        System.out.println("用户" + msg.getUserId() + ": 请求登录....");
        if (validate(msg)) {
            responsePacket.setIsSuccess(true);
            responsePacket.setReason("登录成功!!");
        } else {
            responsePacket.setIsSuccess(false);
            responsePacket.setReason("登录失败!!!");
        }
        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean validate(LoginRequestPacket packet) {
        return "青禾".equals(packet.getUsername()) && "8888".equals(packet.getPassword());
    }
}
