package com.xmw.wechat.codec;

import com.xmw.wechat.protocol.common.Packet;
import com.xmw.wechat.protocol.common.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * PacketEncoder 负责响应编码
 *
 * @author mingwei.xia
 * @date 2018/10/11 10:11
 * @since V1.0
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        PacketCodec.encode(msg, out);
    }
}
