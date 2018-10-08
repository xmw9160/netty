package com.xmw.rpc.consumer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xmw.
 * @date 2018/7/14 16:30.
 */
public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    private Object result;
    public Object getResult() {
        return this.result;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.result = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
