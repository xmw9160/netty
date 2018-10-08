package com.xmw.catalina.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author xmw.
 * @date 2018/7/8 10:27.
 */
public class Tomcat {

    public static void main(String[] args) {
        try {
            new Tomcat().start(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(int port) throws Exception {
        // NIO
//        ServerSocketChannel s = ServerSocketChannel.open();
//        s.bind();
        // BIO
//        ServerSocket socket = new ServerSocket(8080);

        // Netty 主从
        // boss 线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // netty 服务
            ServerBootstrap server = new ServerBootstrap();
            // 链路式编程
            server.group(bossGroup, workerGroup)
                    // 主线程处理类
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) {
                            // 无锁化串行编程
                            // 业务逻辑链路
                            // 编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // 解码器
                            client.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            client.pipeline().addLast(new XmwTomcatHandler());
                        }
                    })
                    // 配置主线程. 128分配线程的最大个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 子线程配置. 长连接服务
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 阻塞
            ChannelFuture sync = server.bind(port).sync();
            System.out.println("tomcat " + port + " 已经启动......");
            sync.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
