package com.xmw.start;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

/**
 * 05NettyClientStart
 *
 * @author mingwei.xia
 * @date 2018/10/8 16:18
 * @since V1.0
 */
public class NettyClientStart {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        AttributeKey<Object> clientKey = AttributeKey.newInstance("clientStartName");
        // 负责启动客户端以及连接服务端
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 设置线程模型(池)
                // 1.指定线程模型, 指定线程模型，驱动着连接的数据读写，
                // 这个线程的概念可以和第一小节Netty是什么中的 IOClient.java 创建的线程联系起来
                // 创建线程用于创建连接
                .group(bossGroup)
                .attr(clientKey, "nettyClientStart")
                // 设置客户端线程模型. oio: OioSocketChannel.class.
                // 2.指定 IO 类型为 NIO
                // 指定 IO 模型为 NioSocketChannel，表示 IO 模型为 NIO
                .channel(NioSocketChannel.class)
                // option() 方法可以给连接设置一些 TCP 底层相关的属性
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)  //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.SO_KEEPALIVE, true)  // 是否开启 TCP 底层心跳机制，true 为开启
                // 表示是否开启Nagle算法，true 表示关闭，false 表示开启，通俗地说，如果要求高实时性，
                // 有数据发送时就马上发送，就设置为 true 关闭，如果需要减少发送次数减少网络交互，就设置为 false 开启
                .option(ChannelOption.TCP_NODELAY, true)
                // 3.IO 处理逻辑
                // 给引导类指定一个 handler，这里主要就是定义连接的业务处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        System.out.println(ch.attr(clientKey).get());
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        // 4.建立连接
//        ChannelFuture channelFuture = bootstrap
////                .connect("juejin.im", 80)
//                .connect("127.0.0.1", 8085)
//                .addListener(future -> {
//                    if (future.isSuccess()) {
//                        System.out.println("连接成功!");
//                    } else {
//                        System.out.println("连接失败!");
//                    }
//                });

//        Channel channel = channelFuture.channel();
        Channel channel = connect(bootstrap, "127.0.0.1", 8085, 5).channel();
        while (true) {
            String sendMsg = "当前时间为: " + new Date();
            System.out.println(sendMsg);
            channel.writeAndFlush(sendMsg);
            TimeUnit.SECONDS.sleep(2);
        }
    }

    /**
     * 连接服务器, 连接失败进行重连
     */
    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture connect = bootstrap.connect(host, port);
        connect.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 重试次数
                int retryCount = (MAX_RETRY - retry) + 1;
                // 延迟时间
//                int delay = 2 << retryCount;
                int delay = 2;
                System.out.println("连接失败, " + delay + "秒后进行第" + retryCount + "次重试!");
                // 进行重新连接
                // BootstrapConfig，他是对 Bootstrap 配置参数的抽象，
                // 然后 bootstrap.config().group() 返回的就是我们在一开始的时候配置的线程模型 workerGroup，
                // 调 workerGroup 的 schedule 方法即可实现定时任务逻辑.
                bootstrap.config().group().schedule(() -> {
                    connect(bootstrap, host, port, retry - 1);
                }, delay, TimeUnit.SECONDS);
            }
        });
        return connect;
    }
}
