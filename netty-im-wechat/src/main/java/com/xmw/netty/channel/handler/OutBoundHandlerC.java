package com.xmw.netty.channel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * OutBoundHandlerC
 *
 * @author mingwei.xia
 * @date 2018/10/10 17:52
 * @since V1.0
 */
public class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutBoundHandlerC: " + msg);
        // io.netty.channel.AbstractChannelHandlerContext.write(java.lang.Object, boolean, io.netty.channel.ChannelPromise)
        // 调用下一个outboundHandler的wirte方法, 并且会把当前 outBoundHandler 里处理完毕的对象传递到下一个 outBoundHandler。
        super.write(ctx, msg, promise);
    }
}
