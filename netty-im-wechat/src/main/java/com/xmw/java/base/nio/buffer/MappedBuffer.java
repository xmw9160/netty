package com.xmw.java.base.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xmw.
 * @date 2018/6/10 13:31.
 */
public class MappedBuffer {

    private static final int start = 0;
    private static final int size = 1024;
    public static void main(String[] args) throws Exception{
        RandomAccessFile raf = new RandomAccessFile("d://info.txt", "rw");
        FileChannel fc = raf.getChannel();

        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);

        mbb.put(0, (byte) 97);
        mbb.put(3, (byte) 122);

        raf.close();
    }
}
