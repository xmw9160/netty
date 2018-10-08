package com.xmw.exchange;

import com.xmw.exchange.handler.FirstClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * NettyExchangeClient
 *
 * @author mingwei.xia
 * @date 2018/10/8 13:41
 * @since V1.0
 */
public class NettyExchangeClient {
    public static void main(String[] args) {
        // group对应了我们IOClient.java中 main 函数起的线程
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new FirstClientHandler());
                    }
                });
        bootstrap.connect("127.0.0.1", 8000);
    }
}
