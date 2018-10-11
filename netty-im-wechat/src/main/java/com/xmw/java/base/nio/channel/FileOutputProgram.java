package com.xmw.java.base.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xmw.
 * @date 2018/6/10 14:11.
 */
public class FileOutputProgram {
     private static final byte message[] = { 83, 111, 109, 101, 32,
            98, 121, 116, 101, 115, 46 };
    public static void main(String[] args) throws Exception{

        FileOutputStream fout = new FileOutputStream("d://info.txt");

        FileChannel channel = fout.getChannel();

//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//
//        for (int i = 0; i < message.length; i++) {
//            buffer.put((byte) i);
//        }
        ByteBuffer buffer = ByteBuffer.wrap(message);
//        buffer.flip();
        channel.write(buffer);
        fout.close();
    }
}
