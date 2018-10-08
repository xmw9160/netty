package com.xmw.exchange.handler;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * FirstClientHandler
 *
 * @author mingwei.xia
 * @date 2018/10/8 17:55
 * @since V1.0
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    private static final Charset charset = Charset.forName("utf-8");

    @Override
    // channelActive()方法，这个方法会在客户端连接建立成功之后被调用
    // 在客户端连接成功之后会回调到逻辑处理器的 channelActive() 方法
    public void channelActive(ChannelHandlerContext ctx) {
        // 1. 获取数据
        ByteBuf buffer = getByteBuf(ctx);
        System.out.println(new Date() + ": 客户端写出数据: " + buffer.toString(charset));
        // 2. 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    // 不管是服务端还是客户端，收到数据之后都会调用到 channelRead 方法
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客服端收到数据: " + byteBuf.toString(charset));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();
        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = "你好, 成都".getBytes(charset);
        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);
        return buffer;
    }
}
