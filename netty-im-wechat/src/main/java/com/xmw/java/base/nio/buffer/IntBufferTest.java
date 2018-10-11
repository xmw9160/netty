package com.xmw.java.base.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author xmw.
 * @date 2018/6/10 12:19.
 */
public class IntBufferTest {

    public static void main(String[] args) {
        // 分配行的int缓冲区, 参数为缓冲区容量
        // 新缓冲区的当前位置为0, limit=8, capacity=8. 它将具有一个底层实现数组,
        // 其数组的偏移量为0.
        IntBuffer buffer = IntBuffer.allocate(8);

        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            // 将给定的整数写入此缓冲区的当前位置, 当前位置递增
            buffer.put(j);
        }

        // 重设此缓冲区, 将limit设置为position, 然后将position设置为0.
        buffer.flip();

        // 查看当前位置和limit位置之间是否有元素
        while (buffer.hasRemaining()) {
            // 读取缓冲区当前位置的整数, 然后在当前位置递增
            int j = buffer.get();
            System.out.print(j + "  ");
        }
    }
}
