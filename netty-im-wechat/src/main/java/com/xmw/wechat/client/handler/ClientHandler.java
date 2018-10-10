package com.xmw.wechat.client.handler;

import java.util.Date;

import com.xmw.wechat.protocol.LoginRequestPacket;
import com.xmw.wechat.protocol.LoginResponsePacket;
import com.xmw.wechat.protocol.common.Packet;
import com.xmw.wechat.protocol.common.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ClientHandler
 *
 * @author mingwei.xia
 * @date 2018/10/10 10:40
 * @since V1.0
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 测试发送数据
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeInt(8888);
//        ctx.channel().writeAndFlush(buffer);
//        buffer.clear();

        // 发送登录请求数据
        System.out.println(new Date() + ": 客户端开始登录");
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(1);
        packet.setUsername("青禾");
        packet.setPassword("8888");
        ByteBuf byteBuf = PacketCodec.encode(packet);
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket responsePacket = (LoginResponsePacket) packet;
            if (responsePacket.getIsSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + responsePacket.getReason());
            }
        }
    }
}