package com.xmw.chat.server.handler;

import com.xmw.chat.process.IMProcessor;
import com.xmw.chat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xmw.
 * @date 2018/7/11 23:33.
 */
public class SocketHandler extends SimpleChannelInboundHandler<IMMessage>{

    private IMProcessor processor = new IMProcessor();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) {
        processor.process(ctx.channel(), msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

    }
}
