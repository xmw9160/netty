package com.xmw.wechat.client.handler;

import java.util.Date;
import java.util.Random;

import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

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
//        // 发送登录请求数据
        System.out.println(new Date() + ": 客户端开始登录");
        LoginRequestPacket packet = new LoginRequestPacket();
        int userId = new Random().nextInt(317);
        packet.setUserId(userId);
        packet.setUsername("青禾" + userId);
        packet.setPassword("8888");
        ctx.channel().writeAndFlush(packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        String userId = msg.getUserId();
        String userName = msg.getUserName();
        if (msg.getIsSuccess()) {
            SessionUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功");
            System.out.println("[" + userName + "]登录成功，userId 为: " + msg.getUserId());
            // 客户端绑定session和channel的对应关系
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }
}
