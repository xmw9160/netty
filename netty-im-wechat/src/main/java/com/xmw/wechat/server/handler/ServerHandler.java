package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.common.Packet;
import com.xmw.wechat.protocol.common.PacketCodec;
import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.request.MessageRequestPacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * ServerHandler
 *
 * @author mingwei.xia
 * @date 2018/10/10 10:41
 * @since V1.0
 */
@Deprecated
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 测试接收数据
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("接收到数据: " + byteBuf.getInt(0));

        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.decode(byteBuf);
        if (packet instanceof LoginRequestPacket) {
            LoginResponsePacket responsePacket = new LoginResponsePacket();
            responsePacket.setVersion(packet.getVersion());
            LoginRequestPacket requestPacket = (LoginRequestPacket) packet;
            if (validate(requestPacket)) {
                responsePacket.setIsSuccess(true);
                responsePacket.setReason("登录成功!");
            } else {
                responsePacket.setIsSuccess(false);
                responsePacket.setReason("登录失败!!");
            }
            ctx.channel().writeAndFlush(PacketCodec.encode(responsePacket, ctx.alloc().buffer()));
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket requestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + " : 收到客户端信息: " + requestPacket.getMessage());

            MessageResponsePacket responsePacket = new MessageResponsePacket();
            responsePacket.setMessage("服务端回复【" + requestPacket.getMessage() + "】");
            ctx.channel().writeAndFlush(PacketCodec.encode(responsePacket, ctx.alloc().buffer()));
        }
    }

    private boolean validate(LoginRequestPacket packet) {
        return "青禾".equals(packet.getUserName()) && "8888".equals(packet.getPassword());
    }
}
