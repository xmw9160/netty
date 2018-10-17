package com.xmw.wechat.server;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.xmw.wechat.codec.PacketCodecHandler;
import com.xmw.wechat.codec.Spliter;
import com.xmw.wechat.server.handler.LifeCyCleTestHandler;
import com.xmw.wechat.server.handler.optimize.AuthHandler;
import com.xmw.wechat.server.handler.optimize.IMHandler;
import com.xmw.wechat.server.handler.optimize.LoginRequestHandler;
import com.xmw.wechat.util.ClientCountUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * WechatOptimizeServer
 *
 * @author mingwei.xia
 * @date 2018/10/10 9:58
 * @since V1.0
 */
@SuppressWarnings("Duplicates")
public class WechatOptimizeServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    // 每次有新连接到来的时候，都会调用ChannelInitializer的initChannel()方法.
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 测试handler生命周期
                        pipeline.addLast(new LifeCyCleTestHandler());
                        // 长度域拆包器-过滤自定义协议请求
                        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new Spliter());
                        // 请求编解码
                        pipeline.addLast(PacketCodecHandler.INSTANCE);
                        // 登录请求处理
                        pipeline.addLast(LoginRequestHandler.INSTANCE);
                        // 认证处理
                        pipeline.addLast(AuthHandler.INSTANCE);
                        // Handler处理
                        pipeline.addLast(IMHandler.INSTANCE);
                    }
                });
        bootstrap.bind(PORT).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("wechat server start success bind port: " + PORT);

                // 每两秒打印一次当前客户端连接数
                Executors.newSingleThreadScheduledExecutor()
                        .scheduleWithFixedDelay(ClientCountUtil::printClientInfo, 10, 30, TimeUnit.SECONDS);
            } else {
                System.out.println("wechat server start failure bind port: " + PORT);
            }
        });
    }
}
