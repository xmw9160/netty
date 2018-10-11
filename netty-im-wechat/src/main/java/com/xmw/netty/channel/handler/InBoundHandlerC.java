package com.xmw.netty.channel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * InBoundHandlerC
 *
 * @author mingwei.xia
 * @date 2018/10/10 17:35
 * @since V1.0
 */
public class InBoundHandlerC extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("InBoundHandlerC: " + msg + ", time: " + ctx.channel().attr(Constants.TIME).get());
        ctx.channel().writeAndFlush(msg);
    }
}
