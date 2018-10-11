package com.xmw.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * IOServer
 * 1. 线程资源受限：线程是操作系统中非常宝贵的资源，同一时刻有大量的线程处于阻塞状态是非常严重的资源浪费，操作系统耗不起
 * 2. 线程切换效率低下：单机 CPU 核数固定，线程爆炸之后操作系统频繁进行线程切换，应用性能急剧下降。
 * 3. 除了以上两个问题，IO 编程中，我们看到数据读写是以字节流为单位，效率不高。...
 *
 * @author mingwei.xia
 * @date 2018/10/8 10:44
 * @since V1.0
 */
public class IOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);

        //1. 接收新连接线程
        new Thread(() -> {
            while (true) {
                try {
                    //(1) 以阻塞方法获取新连接
                    Socket socket = serverSocket.accept();

                    //(2) 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try {
                            int len = 0;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            // (3) 按字节流方式读取数据
                            // 每个线程都有一个while循环读取数据
                            while ((len = inputStream.read(data)) != -1) {
                                System.out.println("接收数据: " + new String(data, 0, len));
                            }
                            OutputStream outputStream = socket.getOutputStream();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
