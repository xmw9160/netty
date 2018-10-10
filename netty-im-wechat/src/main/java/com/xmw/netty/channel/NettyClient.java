package com.xmw.netty.channel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

/**
 * NettyClient
 *
 * @author mingwei.xia
 * @date 2018/10/8 13:41
 * @since V1.0
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        // group对应了我们IOClient.java中 main 函数起的线程
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .attr(AttributeKey.newInstance("client"), "client")
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                    }
                });
        bootstrap.connect("127.0.0.1", 8000)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        ((ChannelFuture)future).channel().writeAndFlush("客户端数据...");
                    }
                });
    }
}
