package com.xmw.code.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 22:38.
 */
// 扩展ByteToMessageDecoder 类，以将字节解码为特定的格式
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 检查是否至少有4字节可读（一个 int的字节长度）
        if (in.readableBytes() >= 4) {
            // 从入站ByteBuf中读取一个 int，并将其添加到解码消息的 List 中
            out.add(in.readInt());
        }
    }
}
