package com.xmw.java.base.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author xmw.
 * @date 2018/6/10 13:09.
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 缓冲0-9
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        // 创建只读缓冲区
        ByteBuffer readOnly = buffer.asReadOnlyBuffer();

        // 改变只读缓冲区数据
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readOnly.position(0);
        readOnly.limit(buffer.capacity());

        // 读取只读缓冲区
        while (readOnly.hasRemaining()) {
            System.out.println(readOnly.get() + "   ");
        }
    }
}
