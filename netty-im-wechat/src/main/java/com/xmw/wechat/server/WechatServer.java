package com.xmw.wechat.server;

import com.xmw.wechat.server.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * WechatServer
 *
 * @author mingwei.xia
 * @date 2018/10/10 9:58
 * @since V1.0
 */
public class WechatServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServerHandler());
                    }
                });
        bootstrap.bind(PORT).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("wechat server start success bind port: " + PORT);
            } else {
                System.out.println("wechat server start failure bind port: " + PORT);
            }
        });
    }
}
