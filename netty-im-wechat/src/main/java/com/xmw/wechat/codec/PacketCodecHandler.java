package com.xmw.wechat.codec;

import java.util.List;

import com.xmw.wechat.protocol.common.Packet;
import com.xmw.wechat.protocol.common.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * 编解码处理器
 * MessageToMessageCodec 同时实现了ChannelInboundHandler和ChannelOutboundHandler
 *
 * @author mingwei.xia
 * @date 2018/10/17 16:22
 * @since V1.0
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) {
        ByteBuf buffer = ctx.alloc().ioBuffer();
        PacketCodec.encode(msg, buffer);
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        out.add(PacketCodec.decode(msg));
    }
}
