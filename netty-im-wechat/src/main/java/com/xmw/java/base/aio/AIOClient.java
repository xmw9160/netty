package com.xmw.java.base.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xmw.
 * @date 2018/6/10 15:45.
 */
public class AIOClient {
    private final AsynchronousSocketChannel client;

    public AIOClient() throws Exception {
        //Asynchronous
        //BIO   Socket
        //NIO   SocketChannel
        //AIO   AsynchronousSocketChannel
        //先把高速公路修起来
        client = AsynchronousSocketChannel.open();
    }

    public static void main(String[] args) throws Exception {
//        new AIOClient().connect("localhost", 8000);

        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                latch.countDown();
                try {
                    new AIOClient().connect("localhost", 8000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        latch.await();
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    public void connect(String host, int port) throws Exception {
        //开始发车，连上高速公路
        //Viod什么都不是
        //也是实现一个匿名的接口
        //这里只做写操作
        client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            //实现IO操作完成的方法
            @Override
            public void completed(Void result, Object attachment) {
                try {
                    Future<Integer> write = client.write(ByteBuffer.wrap("这是一条测试数据".getBytes()));
                    write.get();
                    System.out.println("数据已发送到服务器...");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            //实现IO操作失败的方法
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        //下面这一段代码是只读数据
        final ByteBuffer bb = ByteBuffer.allocate(1024);
        client.read(bb, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("IO操作成功: " + result);
                System.out.println("获取反馈结果: " + new String(bb.array()));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
