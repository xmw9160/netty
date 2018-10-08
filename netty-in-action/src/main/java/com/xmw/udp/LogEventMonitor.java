package com.xmw.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/8/23 23:23.
 */
@Slf4j
public class LogEventMonitor {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    private LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                // 引导该 NioDatagramChannel
                .channel(NioDatagramChannel.class)
                // 设置套接字选项SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 将LogEventDecoder和LogEventHandler添加到ChannelPipeline 中
                        pipeline.addLast(new LogEventDecoder());
                        pipeline.addLast(new LogEventHandler());
                    }
                }).localAddress(address);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: LogEventMonitor <port>");
        }
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(Integer.parseInt(args[0])));
        try {
            Channel channel = monitor.bind();
            log.warn("LogEventMonitor running with port : " + args[0]);
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }

    // 绑定Channel。注意，DatagramChannel是无连接的
    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    private void stop() {
        group.shutdownGracefully();
    }
}