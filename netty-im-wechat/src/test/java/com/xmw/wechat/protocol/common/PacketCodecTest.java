package com.xmw.wechat.protocol.common;

import org.junit.Assert;
import org.junit.Test;

import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.serialize.Serializer;
import com.xmw.wechat.serialize.impl.JsonSerializer;

import io.netty.buffer.ByteBuf;

/**
 * 测试编解码
 *
 * @author mingwei.xia
 * @date 2018/10/9 17:34
 * @since V1.0
 */
public class PacketCodecTest {

    @Test
    public void encode() {
        Serializer serializer = new JsonSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId(111);
        loginRequestPacket.setUserName("username");
        loginRequestPacket.setPassword("password");

        ByteBuf byteBuf = PacketCodec.encode(loginRequestPacket, null);
        Packet decodedPacket = PacketCodec.decode(byteBuf);
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));
    }
}