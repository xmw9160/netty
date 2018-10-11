package com.xmw.java.base.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author xmw.
 * @date 2018/6/10 20:08.
 */
public class NIOClient1 {
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPILIT = "#@#";
    private final InetSocketAddress serverAdrress = new InetSocketAddress("localhost", 8080);
    private Selector selector = null;
    private SocketChannel client = null;
    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");

    public NIOClient1() throws IOException {
        // 连接远程主机的ip和端口
        client = SocketChannel.open();
        client.connect(serverAdrress);
        client.configureBlocking(false);

        // 开门接客
        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        new NIOClient1().session();
    }

    public void session() {
        // 开辟一个新线程从服务器端读数据
        new Reader().start();
        //开辟一个新线程往服务器端写数据
        new Writer().start();
    }

    private class Reader extends Thread {
        @Override
        public void run() {
            try {
                //轮询
                while (true) {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) continue;
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();  //可以通过这个方法，知道可用通道的集合
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        process(key);
                    }
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        private void process(SelectionKey key) throws IOException {
            if (key.isReadable()) {
                //使用 NIO 读取 Channel中的数据，这个和全局变量client是一样的，因为只注册了一个SocketChannel
                //client既能写也能读，这边是读
                SocketChannel sc = (SocketChannel) key.channel();

                ByteBuffer buff = ByteBuffer.allocate(1024);
                StringBuilder content = new StringBuilder();
                while (sc.read(buff) > 0) {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                //若系统发送通知名字已经存在，则需要换个昵称
                if (USER_EXIST.equals(content.toString())) {
                    nickName = "";
                }
                System.out.println(content);
                // 重新注册为可读事件
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private class Writer extends Thread {
        @Override
        public void run() {
            // 在主线程中, 从键盘读取数据输入到服务器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if ("".equals(line)) {
                    continue;
                }
                if ("".equals(nickName)) {
                    nickName = line;
                    line = nickName + USER_CONTENT_SPILIT;
                } else {
                    line = nickName + USER_CONTENT_SPILIT + line;
                }
//                client.register(selector, SelectionKey.OP_WRITE);
                try {
                    client.write(charset.encode(line));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            scanner.close();
        }
    }
}
