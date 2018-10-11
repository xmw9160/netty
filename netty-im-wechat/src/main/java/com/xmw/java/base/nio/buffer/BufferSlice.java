package com.xmw.java.base.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author xmw.
 * @date 2018/6/10 12:58.
 */
public class BufferSlice {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 缓冲区的数据为0-9
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) i);
        }

        // 创建子缓冲区
        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();

        // 改变子缓冲区的内容
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + "   ");
        }
    }
}
