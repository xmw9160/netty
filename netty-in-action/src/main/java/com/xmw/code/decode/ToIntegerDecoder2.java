package com.xmw.code.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 22:44.
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    // 传入的 ByteBuf 是ReplayingDecoderByteBuf
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 从入站ByteBuf中读取一个int，并将其添加到解码消息的List中
        out.add(in.readInt());
    }
}
