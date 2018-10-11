package com.xmw.java.base.nio.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xmw.
 * @date 2018/6/10 14:02.
 */
public class FileInputProgram {
    public static void main(String[] args) throws Exception{
        FileInputStream fin = new FileInputStream("d://info.txt");

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(40);

        // 读取数据到缓冲区
//        fc.read(buffer);
//
//        buffer.flip();
//
//        while (buffer.hasRemaining()) {
//            System.out.print((char) buffer.get());
//        }

        while (fc.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println();
            buffer.clear();
        }


    }
}
