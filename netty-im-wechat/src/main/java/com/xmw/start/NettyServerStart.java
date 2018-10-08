package com.xmw.start;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

/**
 * 04服务端启动流程
 *
 * @author mingwei.xia
 * @date 2018/10/8 14:33
 * @since V1.0
 */
public class NettyServerStart {
    public static void main(String[] args) {
        // bossGroup接收完连接，扔给workerGroup去处理
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap
                //给引导类配置两大线程组，这个引导类的线程模型也就定型了。
                .group(bossGroup, workerGroup)
                // 指定我们服务端的 IO 模型为NIO, BIO : OioServerSocketChannel.class
                .channel(NioServerSocketChannel.class)
                // attr()方法可以给服务端的 channel，也就是NioServerSocketChannel指定一些自定义属性，
                // 然后我们可以通过channel.attr()取出这个属性
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                // childAttr可以给每一条连接指定自定义属性，然后后续我们可以通过channel.attr()取出该属性
                .childAttr(clientKey, "clientValue")
                // 给服务端channel设置一些属性，最常见的就是so_backlog
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，
                // 如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // childOption()可以给每条连接设置一些TCP底层相关的属性
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //是否开启TCP底层心跳机制，true为开启
                // 是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，
                // 有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                .childOption(ChannelOption.TCP_NODELAY, true)
                // handler()用于指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法。
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务端启动中....");
                    }
                })
                // childHandler()用于指定处理新连接数据的读写处理逻辑
                // 这里主要就是定义后续每条连接的数据读写，业务处理逻辑
                // NioSocketChannel类是Netty 对 NIO 类型的连接的抽象
                // NioServerSocketChannel和NioSocketChannel的概念可以
                // 和 BIO 编程模型中的ServerSocket以及Socket两个概念对应上
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println(ch.attr(clientKey).get());

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println("收到数据: " + msg);
                            }
                        });
                    }
                });
        // 它是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture
        // ChannelFuture channelFuture = serverBootstrap.bind(8000);
        bindPort(serverBootstrap, 8085);
    }

    private static void bindPort(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口:" + port + "绑定成功!");
            } else {
                System.out.println("端口:" + port + "绑定失败!");
                bindPort(serverBootstrap, port + 1);
            }
        });
    }
}
