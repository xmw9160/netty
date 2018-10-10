package com.xmw.netty.channel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * InBoundHandlerA
 *
 * @author mingwei.xia
 * @date 2018/10/10 17:34
 * @since V1.0
 */
public class InBoundHandlerA extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InBoundHandlerA: " + msg);
        // ctx.fireChannelRead(msg);
        // 调用下一个handler的channelRead方法, 并且会把当前 inBoundHandler 里处理完毕的对象传递到下一个 inBoundHandler
        super.channelRead(ctx, msg);
    }
}
