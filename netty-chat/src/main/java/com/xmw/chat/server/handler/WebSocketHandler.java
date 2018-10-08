package com.xmw.chat.server.handler;

import com.xmw.chat.process.IMProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xmw.
 * @date 2018/7/8 23:19.
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    private IMProcessor processor = new IMProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        processor.process(ctx.channel(), msg.text());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("有人退出了聊天室....");
        processor.logout(ctx.channel());
    }
}
