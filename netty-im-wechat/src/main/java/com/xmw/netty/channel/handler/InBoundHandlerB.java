package com.xmw.netty.channel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * InBoundHandlerB
 *
 * @author mingwei.xia
 * @date 2018/10/10 17:35
 * @since V1.0
 */
public class InBoundHandlerB extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerB: " + msg + ", time: " + ctx.channel().attr(Constants.TIME).get());
        ctx.channel().attr(Constants.TIME).set(System.currentTimeMillis());
        super.channelRead(ctx, msg);
    }
}
