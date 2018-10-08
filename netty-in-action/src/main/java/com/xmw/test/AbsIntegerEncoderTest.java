package com.xmw.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author xmw.
 * @date 2018/8/7 23:25.
 */
class AbsIntegerEncoderTest {
    @Test
    void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        // 创建一个 ByteBuf，并且写入 9 个负整数
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        // 创建一个EmbeddedChannel，并安装一个要测试的AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        // 写入 ByteBuf，并断言调用readOutbound()方法将会产生数据
        assertTrue(channel.writeOutbound(buf));
        // 将该Channel标记为已完成状态
        assertTrue(channel.finish());
        // read bytes 读取所产生的消息，并断言它们包含了对应的绝对值
        for (int i = 1; i  < 10; i++) {
            assertEquals(-i, (int)channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}
