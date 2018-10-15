package com.xmw.java.base.nio.buffer;

import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xmw.
 * @date 2018/6/10 12:44.
 */
public class BufferProgram {
    public static void main(String[] args) throws Exception {
        FileInputStream fin = new FileInputStream("d://info.txt");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        output("初始化", buffer);

        // 从通道中读取数据到buffer
        fc.read(buffer);
        output("调用read()", buffer);

        buffer.flip();
        output("调用flip()", buffer);

        while (buffer.hasRemaining()) {
            byte b = buffer.get();
        }
        output("调用get()", buffer);

        buffer.clear();
        output("调用clear()", buffer);

        fin.close();

    }

    public static void output(String step, Buffer buffer) {
        System.out.println(step + " : ");
        System.out.print("capacity: " + buffer.capacity() + ", ");
        System.out.print("position: " + buffer.position() + ", ");
        System.out.print("limit: " + buffer.limit());
        System.out.println();
    }
}
