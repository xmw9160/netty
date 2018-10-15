package com.xmw.java.base.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xmw.
 * @date 2018/6/10 15:30.
 */
public class AIOServer {
    private final int port;

    public AIOServer(int port) {
        this.port = port;
        listen();
    }

    public static void main(String[] args) {
        new AIOServer(8000);
    }

    private void listen() {
        try {
            // 线程缓冲池，为了体现异步
            ExecutorService executorService = Executors.newCachedThreadPool();
            // 给线程池初始化一个线程
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);

            //Asynchronous异步
            //NIO   ServerSocketChannel
            //BIO   ServerSocket   有那么一点点像

            // 创建通道
            AsynchronousServerSocketChannel channel = AsynchronousServerSocketChannel.open(threadGroup);
            // 绑定socket地址端口
            InetSocketAddress socketAddress = new InetSocketAddress(port);
            channel.bind(socketAddress);
            System.out.println("服务已启动, 监听端口: " + port);

            final Map<String, Integer> count = new ConcurrentHashMap<>();
            count.put("count", 0);
            //开始等待客户端连接
            //实现一个CompletionHandler 的接口，匿名的实现类
            channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                //实现IO操作完成的方法
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    count.put("count", count.get("count") + 1);
                    System.out.println("IO 操作成功, 开始获取数据-" + count.get("count"));
                    try {
                        buffer.clear();
                        Future<Integer> read = result.read(buffer);
                        // 获取执行结果
                        read.get();
                        buffer.flip();
                        result.write(buffer);
                        buffer.flip();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            result.close();
                            channel.accept(null, this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("操作完成...");
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("IO操作失败: " + exc);
                }
            });

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
