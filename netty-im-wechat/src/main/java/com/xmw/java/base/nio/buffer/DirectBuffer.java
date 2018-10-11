package com.xmw.java.base.nio.buffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xmw.
 * @date 2018/6/10 13:19.
 */
public class DirectBuffer {
    public static void main(String[] args) throws Exception {
        String inFile = "d://info.txt";
        FileInputStream fin = new FileInputStream(inFile);
        FileChannel fcin = fin.getChannel();

        String outFile = "d://infoCopy.txt";
        File file = new File(outFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fout = new FileOutputStream(outFile);

        FileChannel fcout = fout.getChannel();

        // 使用allocateDirect, 而不是allocate
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            buffer.clear();
            int r = fcin.read(buffer);
            if (r == -1) {
                System.out.println("文件复制完成....");
                break;
            }
            buffer.flip();
            fcout.write(buffer);
        }
    }
}
