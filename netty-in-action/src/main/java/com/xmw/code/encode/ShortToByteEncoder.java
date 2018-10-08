package com.xmw.code.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xmw.
 * @date 2018/8/20 23:05.
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    public void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) {
        // 将Short写入ByteBuf 中
        out.writeShort(msg);
    }
}
