package com.xmw.java.base.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @author xmw.
 * @date 2018/6/10 18:14.
 */
public class BIOClient {

    public static void main(String[] args) {
        try {
            // 连接到socket服务器, 开一条乡村公路
            Socket client = new Socket("localhost", 8080);
            // 输出流通道打开
            OutputStream os = client.getOutputStream();
            // 生成UUID
            String uuid = UUID.randomUUID().toString();

            // 发送给服务器
            os.write(uuid.getBytes());

            // 获取服务器返回的书籍
            InputStream is = client.getInputStream();
            byte[] buffer = new byte[1024];
            int len = is.read(buffer);
            System.out.println("data form server: " + new String(buffer, 0, len));
            os.close();
            is.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
