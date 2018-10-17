package com.xmw.wechat.server.handler;

import com.xmw.wechat.protocol.request.HeartBeatRequestPacket;
import com.xmw.wechat.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端响应客户端心跳检查
 *
 * @author mingwei.xia
 * @date 2018/10/17 17:45
 * @since V1.0
 */
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) {
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket());
    }
}
