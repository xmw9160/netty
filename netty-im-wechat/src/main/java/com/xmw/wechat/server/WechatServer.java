package com.xmw.wechat.server;

import com.xmw.wechat.codec.PacketDecoder;
import com.xmw.wechat.codec.PacketEncoder;
import com.xmw.wechat.codec.Spliter;
import com.xmw.wechat.server.handler.LoginRequestHandler;
import com.xmw.wechat.server.handler.MessageRequestHandler;

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
                        // 长度域拆包器-过滤自定义协议请求
                        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new Spliter());
                        // 请求解码
                        pipeline.addLast(new PacketDecoder());
                        // 登录请求处理
                        pipeline.addLast(new LoginRequestHandler());
                        // 消息请求处理
                        pipeline.addLast(new MessageRequestHandler());
                        // 响应编码
                        pipeline.addLast(new PacketEncoder());
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
