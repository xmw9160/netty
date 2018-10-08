package com.xmw.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/8/6 22:58.
 */
public class ServerDemo {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 创建ServerBootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置EventLoopGroup，其提供了用于处理Channel 事件的EventLoop
        bootstrap.group(group)
                // 指定要使用的Channel 实现
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子Channel的I/O及数据的 ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) {
                        System.out.println("Received data");
                    }
                } );
        // 通过配置好的ServerBootstrap的实例绑定该Channel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bound attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        } );
    }
}
