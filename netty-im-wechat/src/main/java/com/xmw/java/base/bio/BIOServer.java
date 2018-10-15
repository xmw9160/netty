package com.xmw.java.base.bio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xmw.
 * @date 2018/6/10 18:06.
 */
public class BIOServer {

    private ServerSocket server;

    public BIOServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听端口是：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new BIOServer(8080).listener();
    }

    // 开始监听端口, 并处理逻辑
    private void listener() throws Exception {
        // 死循环监听
        while (true) {
            //虽然写了一个死循环，如果一直没有客户端连接的话，这里一直不会往下执行
            Socket client = server.accept();

            // 拿到输入流, 也就是乡村公路
            InputStream is = client.getInputStream();

            // 缓冲区, 数组而已
            byte[] buff = new byte[1024];
            int len = is.read(buff);
            if (len > 0) {
                System.out.println("收到数据: " + new String(buff, 0, len));
            }

            // 返回客户端数据
            OutputStream os = client.getOutputStream();
            DataOutputStream outputStream = new DataOutputStream(os);
            outputStream.writeUTF("这是来自ServerSocket返回的数据: 明月松间照, 清泉石上流.");
            outputStream.flush();
            // os.write("这是来自ServerSocket返回的数据: 明月松间照, 清泉石上流.".getBytes());
//            os.close();
//            is.close();
        }
    }
}
