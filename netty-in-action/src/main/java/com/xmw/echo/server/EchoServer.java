package com.xmw.echo.server;

import com.xmw.echo.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/7/19 23:32.
 */
@Slf4j
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: " + EchoServer.class.getSimpleName() +
//                            " <port>");
//        }
//        int port = Integer.parseInt(args[0]);
        // 调用服务器的start()方法
        new EchoServer(8080).start();
    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        // 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个EchoServerHandler到子Channel的ChannelPipeline
                    // 当一个新的连接被接受时，一个新的子Channel将会被创建，而ChannelInitializer将会把一个你的
                    // EchoServerHandler的实例添加到该Channel的ChannelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // EchoServerHandler被标注为@Shareable，
                            // 所以我们可以总是使用同样的实例
                            // 对于所有的客户端连接来说，都会使用同一个 EchoServerHandler，因为其被标注为@Sharable
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(handler);
                        }
                    });
            // 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            // （对 sync()方法的调用将导致当前Thread阻塞，一直到绑定操作完成为止）
            ChannelFuture future = bootstrap.bind().sync();
            // 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            log.info("server 已经启动. 监听的端口为: {}", port);
            future.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup，释放所有的资源
            group.shutdownGracefully();
        }
    }
}
