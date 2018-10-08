package com.xmw.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xmw.
 * @date 2018/8/23 23:20.
 */
// 扩展SimpleChannelInboundHandler 以处理 LogEvent 消息
@Slf4j
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当异常发生时，打印栈跟踪信息，并关闭对应的 Channel
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LogEvent event) {
        // 打印 LogEvent的数据
        log.info("receive info: " + event.toString());
    }
}
