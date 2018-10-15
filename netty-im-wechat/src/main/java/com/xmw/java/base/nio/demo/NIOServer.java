package com.xmw.java.base.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xmw.
 * @date 2018/6/10 19:21.
 */
@SuppressWarnings("Duplicates")
public class NIOServer {

    private int port = 8080;
    private InetSocketAddress address = null;

    private Selector selector = null;

    public NIOServer(int port) throws IOException {
        this.port = port;
        this.address = new InetSocketAddress(this.port);
        // 创建channel
        final ServerSocketChannel channel = ServerSocketChannel.open();
        // 为通道绑定地址
        channel.bind(this.address);
        // 默认为阻塞, 手动设置为非阻塞
        channel.configureBlocking(false);

        // 创建Selector
        this.selector = Selector.open();

        // 将channel注册到selector
        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器准备就绪, 监听的端口是: " + this.port);
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listener();
    }

    private void listener() throws IOException {
        while (true) {
            // 获取待处理的事件
            int wait = this.selector.select();
            if (wait == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // 处理key中对应的事件
                processKey(key);
                // 从队列中移除key
                keyIterator.remove();
            }

        }
    }

    private void processKey(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 准备连接状态
        if (key.isAcceptable()) {
            // 获取通道
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel client = channel.accept();
            // 设置为非阻塞
            client.configureBlocking(false);
            // 注册为下一个事件: 可读
            client.register(this.selector, SelectionKey.OP_READ);
        }

        // 处理可读事件
        if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();

            int len = client.read(buffer);
            if (len > 0) {
                buffer.flip();
                System.out.println("server 接收到数据: " + new String(buffer.array(), 0, len));
                client.register(this.selector, SelectionKey.OP_WRITE);
            }
            buffer.clear();
        }

        // 处理可写事件
        if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();
            client.write(ByteBuffer.wrap("server to client : hello world".getBytes()));
            client.close();
        }
    }
}
