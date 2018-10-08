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
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }
}
