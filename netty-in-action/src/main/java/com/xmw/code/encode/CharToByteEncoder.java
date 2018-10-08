package com.xmw.code.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xmw.
 * @date 2018/8/20 23:25.
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    public void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) {
        // 将Character解码为char，并将其写入到出站 ByteBuf 中
        out.writeChar(msg);
    }
}
