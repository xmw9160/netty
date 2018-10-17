package com.xmw.wechat.client.handler;

import java.util.concurrent.TimeUnit;

import com.xmw.wechat.protocol.request.HeartBeatRequestPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 心跳检查
 *
 * @author mingwei.xia
 * @date 2018/10/17 17:37
 * @since V1.0
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {
    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.executor()  //返回的是当前的 channel 绑定的 NIO 线程
                // 实现了每隔 5 秒，向服务端发送一个心跳数据包，这个时间段通常要比服务端的空闲检测时间的一半要短一些，
                // 我们这里直接定义为空闲检测时间的三分之一，主要是为了排除公网偶发的秒级抖动
                .scheduleAtFixedRate(() -> {
                    ctx.channel().writeAndFlush(new HeartBeatRequestPacket());
                    System.err.println("客户端发送心跳检测...");
                    }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.err.println("客户端收到心跳检测响应数据...");
    }
}
