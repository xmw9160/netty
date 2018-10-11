package com.xmw.java.base.nio.buffer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * Desc
 * Date 2018/1/27.
 * Author xmw .
 * Company 成都白起网络科技 .
 */
public class MappedByteBufferCase {

    public static void main(String[] args) {
        File file = new File("D://data.txt");
        long len = file.length();
        byte[] ds = new byte[(int)len];

        try {
            // FileChannel提供了map方法把文件映射到虚拟内存，
            // 通常情况可以映射整个文件，如果文件比较大，可以进行分段映射。
            MappedByteBuffer mappedByteBuffer =
                    new RandomAccessFile(file, "r")
                    .getChannel()
            // MapMode.READ_ONLY：只读，试图修改得到的缓冲区将导致抛出异常
            // MapMode.READ_WRITE：读/写，对得到的缓冲区的更改最终将写入文件；
                            // 但该更改对映射到同一文件的其他程序不一定是可见的。
            // MapMode.PRIVATE：私用，可读可写,但是修改的内容不会写入文件，
                            // 只是buffer自身的改变，这种能力称之为”copy on write”。
                    .map(FileChannel.MapMode.READ_ONLY, 0, len);
            for (int offset = 0; offset < len; offset++) {
                byte b = mappedByteBuffer.get();
                ds[offset] = b;
            }

            Scanner scanner = new Scanner(new ByteArrayInputStream(ds));
            while (scanner.hasNext()) {
                System.out.println(scanner.next() + "   ");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
