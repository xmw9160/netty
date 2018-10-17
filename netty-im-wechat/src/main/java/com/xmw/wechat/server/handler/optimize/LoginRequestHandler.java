package com.xmw.wechat.server.handler.optimize;

import java.util.UUID;

import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;
import com.xmw.wechat.session.Session;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * LoginRequestHandler
 *
 * @author mingwei.xia
 * @date 2018/10/11 9:51
 * @since V1.0
 */
@SuppressWarnings("Duplicates")
@ChannelHandler.Sharable //加上注解标识，表明该 handler 是可以多个 channel 共享的
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(msg.getVersion());
        responsePacket.setUserName(msg.getUserName());
        System.out.println("用户" + msg.getUserId() + ": 请求登录....");
        if (validate(msg)) {
            String userId = randomUserId();
            responsePacket.setUserId(userId);
            // SessionUtil.markAsLogin(ctx.channel());
            // 服务端绑定用户session和channel的关系
            SessionUtil.bindSession(new Session(userId, msg.getUserName()), ctx.channel());

            responsePacket.setIsSuccess(true);
            responsePacket.setReason("登录成功!!");
        } else {
            responsePacket.setIsSuccess(false);
            responsePacket.setReason("登录失败!!!");
        }
        ctx.channel().writeAndFlush(responsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 移除session
        SessionUtil.unBindSession(ctx.channel());
    }

    private boolean validate(LoginRequestPacket packet) {
        return true;
        // return "青禾".equals(packet.getUsername()) && "8888".equals(packet.getPassword());
    }

    private String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
