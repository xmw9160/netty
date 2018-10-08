package com.xmw.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author xmw.
 * @date 2018/7/14 18:31.
 */
public class TestByteBuffer {

    private final byte[] CONTENT = new byte[1024];
    private int loop = 1800000;

    private void testDirectMemoryPool() {
        long startTime = System.currentTimeMillis();
        ByteBuf poolBuffer;
        for (int i = 0; i < loop; i++) {
            poolBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            poolBuffer.writeBytes(CONTENT);
            poolBuffer.release();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("内存池分配缓冲区耗时" + (endTime - startTime) + "ms.");
    }

    private void testDirectMemory() {
        long startTime2 = System.currentTimeMillis();
        ByteBuf buffer;
        for (int i = 0; i < loop; i++) {
            buffer = Unpooled.directBuffer(1024);
            buffer.writeBytes(CONTENT);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("非内存池分配缓冲区耗时" + (endTime - startTime2) + "ms.");
    }

    public static void main(String[] args) {
        TestByteBuffer testByteBuffer = new TestByteBuffer();
        testByteBuffer.testDirectMemory();
        testByteBuffer.testDirectMemoryPool();
    }
}
