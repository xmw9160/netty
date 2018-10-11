package com.xmw.wechat.client.handler;

import java.util.Date;
import java.util.Random;

import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;
import com.xmw.wechat.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * LoginResponseHandler 登录响应处理
 *
 * @author mingwei.xia
 * @date 2018/10/11 10:37
 * @since V1.0
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 发送登录请求数据
        System.out.println(new Date() + ": 客户端开始登录");
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(new Random().nextInt(317));
        packet.setUsername("青禾");
        packet.setPassword("8888");
        ctx.channel().writeAndFlush(packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        if (msg.getIsSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功");
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
    }
}
