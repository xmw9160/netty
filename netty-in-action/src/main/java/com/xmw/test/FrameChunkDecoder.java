package com.xmw.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/7 23:32.
 */
// 扩展 ByteToMessageDecoder 以将入站字节解码为消息
public class FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxFrameSize;

    // 指定将要产生的帧的最大允许大小
    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int readableBytes = in.readableBytes();
        // 如果该帧太大，则丢弃它并抛出一个 TooLongFrameException……
        if (readableBytes > maxFrameSize) {
            // discard the bytes
            in.clear();
            throw new TooLongFrameException();
        }
        // 否则，从ByteBuf中读取一个新的帧
        ByteBuf buf = in.readBytes(readableBytes);
        // 将该帧添加到解码消息的 List 中
        out.add(buf);
    }
}
