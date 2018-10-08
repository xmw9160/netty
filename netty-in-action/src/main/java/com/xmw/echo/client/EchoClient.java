package com.xmw.echo.client;

import com.xmw.echo.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/7/20 0:20.
 */
@Slf4j
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println(
//                    "Usage: " + EchoClient.class.getSimpleName() +
//                            " <host> <port>");
//            return;
//        }
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
        new EchoClient("localhost", 8080).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 指定EventLoopGroup以处理客户端事件；需要适用于 NIO 的实现
            bootstrap.group(group)
                    // 适用于 NIO 传输的Channel 类型
                    .channel(NioSocketChannel.class)
                    // 设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 在创建Channel时，向 ChannelPipeline中添加一个 EchoClientHandler 实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 连接到远程节点，阻塞等待直到连接完成
            ChannelFuture future = bootstrap.connect().sync();
            // 阻塞，直到Channel 关闭
            future.channel().closeFuture().sync();
        } finally {
            // 关闭线程池并且释放所有的资源
            group.shutdownGracefully();
        }
    }
}
