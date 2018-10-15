package com.xmw.java.base.nio.server;

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
 * @date 2018/6/10 19:53.
 */
@SuppressWarnings("Duplicates")
public class NIOServer2 {
    private int port;
    private InetSocketAddress address;
    private Selector selector;

    public NIOServer2(int port) throws IOException {
        this.port = port;
        this.address = new InetSocketAddress(this.port);
        ServerSocketChannel channel = ServerSocketChannel.open();
        // 设置channel阻塞模式
        channel.configureBlocking(false);
        // 绑定之地
        channel.bind(this.address);

        // 开启selector
        this.selector = Selector.open();

        // 绑定channel 到selector, 设置默认事件为: 可接收状态
        channel.register(this.selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器准备完毕, 监听的端口是: " + this.port);
    }

    public static void main(String[] args) throws IOException {
        new NIOServer2(8080).listener();
    }

    private void listener() {
        // 持续监听
        while (true) {
            try {
                // 获取待处理的事件数, 没有会阻塞
                int wait = this.selector.select();
                if (wait == 0) {
                    continue;
                }

                // 获取要处理的事件信息
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // 处理事件信息
                    processKey(key);
                    // 从事件列表中移除事件, 事件处理中会重新绑定下一个流程的事件信息
                    keyIterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processKey(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 处理可接收事件
        if (key.isAcceptable()) {
            // 获取通道信息
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel client = channel.accept();
            // 设置阻塞模式
            client.configureBlocking(false);
            // 重新绑定处理事件
            client.register(this.selector, SelectionKey.OP_READ);
        }
        // 处理可读状态事件
        if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            int len = channel.read(buffer);
            if (len > 0) {
                buffer.flip();
                System.out.println("data from client : " + new String(buffer.array(), 0, len));
                channel.register(this.selector, SelectionKey.OP_WRITE);
            }
            buffer.clear();
            channel.close();
        }
        // 处理可写状态事件
        if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(ByteBuffer.wrap("data from server : hello world".getBytes()));
            channel.close();
        }
    }
}
