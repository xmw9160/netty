package com.xmw.exchange.handler;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * FirstServerHandler
 *
 * @author mingwei.xia
 * @date 2018/10/8 18:06
 * @since V1.0
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    private static final Charset charset = Charset.forName("utf-8");

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf out = ctx.alloc().buffer().writeBytes("欢迎连接服务器!!!".getBytes(charset));
        System.out.println(new Date() + ": 服务端写出数据: " + out.toString(charset));
        ctx.channel().writeAndFlush(out);
    }

    @Override
    // 方法在接收到客户端发来的数据之后被回调
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 接收数据
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(charset));
        // 发送数据, 回复数据到客户端
        ByteBuf out = getByteBuf(ctx);
        System.out.println(new Date() + ": 服务端写出数据: " + out.toString(charset));
        ctx.channel().writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes("欢迎来到一座来了就不想走的城市-成都".getBytes(charset));
        return buffer;
    }
}
