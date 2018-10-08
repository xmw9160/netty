package com.xmw.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/8/6 23:06.
 */
public class ServerDemo2 {
    public static void main(String[] args) {
        ServerBootstrap bootstrap;
        // 创建 ServerBootstrap 以创建ServerSocketChannel，并绑定它
        bootstrap = new ServerBootstrap();
        // 设置 EventLoopGroup，其将提供用以处理 Channel 事件的 EventLoop
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                // 指定要使用的Channel 实现
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子Channel的I/O和数据的ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    ChannelFuture connectFuture;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
                        // 创建一个Bootstrap类的实例以连接到远程主机
                        Bootstrap bootstrap = new Bootstrap();
                        // 指定 Channel的实现
                        bootstrap.channel(NioSocketChannel.class).handler(
                                // 为入站I/O设置/ChannelInboundHandler
                                new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(
                                            ChannelHandlerContext ctx, ByteBuf in) {
                                        System.out.println("Received data");
                                    }
                                });
                        // 使用与分配给已被接受的子Channel相同的EventLoop
                        bootstrap.group(ctx.channel().eventLoop());
                        // 连接到远程节点
                        connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
                        if (connectFuture.isDone()) {
                            // 当连接完成时，执行一些数据操作（如代理）
                            System.out.println("do something with the data");
                        }
                    }
                });
        // 通过配置好的ServerBootstrap绑定该ServerSocketChannel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
