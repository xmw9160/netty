package com.xmw.code.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 23:24.
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= 2) {
            // 将一个或者多个Character对象添加到传出的 List 中
            out.add(in.readChar());
        }
    }
}
