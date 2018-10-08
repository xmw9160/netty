package com.xmw.java.io;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * IOClient
 *
 * @author mingwei.xia
 * @date 2018/10/8 10:54
 * @since V1.0
 */
public class IOClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    String sendMsg = System.currentTimeMillis() + " hello world";
                    System.out.println("发送数据: " + sendMsg);
                    socket.getOutputStream().write(sendMsg.getBytes());
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
