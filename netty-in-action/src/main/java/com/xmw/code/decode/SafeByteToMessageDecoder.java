package com.xmw.code.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 22:54.
 */
// 扩展 ByteToMessageDecoder以将字节解码为消息
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readable = in.readableBytes();
        // 检查缓冲区中是否有超过MAX_FRAME_SIZE个字节
        if (readable > MAX_FRAME_SIZE) {
            // 跳过所有的可读字节，抛出TooLongFrameException 并通知ChannelHandler
            in.skipBytes(readable);
            throw new TooLongFrameException("Frame too big!");
        }
        // do something
    }
}
