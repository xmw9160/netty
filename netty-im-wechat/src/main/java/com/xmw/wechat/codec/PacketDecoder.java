package com.xmw.wechat.codec;

import com.xmw.wechat.protocol.common.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * PacketDecoder 负责请求解码
 *
 * @author mingwei.xia
 * @date 2018/10/11 9:57
 * @since V1.0
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(PacketCodec.decode(in));
    }
}
