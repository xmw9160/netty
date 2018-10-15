package com.xmw.java.base.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author xmw.
 * @date 2018/6/10 18:26.
 */
public class BIOClient2 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    latch.countDown();
                    Socket socket = new Socket("127.0.0.1", 8080);
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    DataOutputStream outputStream = new DataOutputStream(os);
                    DataInputStream inputStream = new DataInputStream(is);
                    //os.write(("client thread: " + Thread.currentThread().getName()).getBytes());
                    outputStream.writeUTF("12345667890");
                    outputStream.flush();

                    byte[] buffer = new byte[1024];
                    int len = is.read(buffer);
                    System.out.println("form server " + new String(buffer, 0, len));

//                    os.close();
//                    is.close();
//                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }
        latch.await();
    }
}
