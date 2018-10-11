package com.xmw.wechat.client;

import java.util.Scanner;

import com.xmw.wechat.client.handler.LoginResponseHandler;
import com.xmw.wechat.client.handler.MessageResponseHandler;
import com.xmw.wechat.codec.PacketDecoder;
import com.xmw.wechat.codec.PacketEncoder;
import com.xmw.wechat.codec.Spliter;
import com.xmw.wechat.protocol.common.PacketCodec;
import com.xmw.wechat.protocol.request.MessageRequestPacket;
import com.xmw.wechat.util.LoginUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * WechatClient
 *
 * @author mingwei.xia
 * @date 2018/10/10 9:57
 * @since V1.0
 */
public class WechatClient {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 长度域拆包器
                        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new Spliter());
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(new LoginResponseHandler());
                        pipeline.addLast(new MessageResponseHandler());
                        pipeline.addLast(new PacketEncoder());
                    }
                });
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接服务器成功");
                // 连接成功之后，启动控制台线程
                startConsoleThread(((ChannelFuture) future).channel());
            } else {
                System.out.println("连接服务器失败");
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                // 登录成功
                if (LoginUtil.hasLogin(channel)) {
                    System.out.print("客户端发送消息: ");
                    Scanner sc = new Scanner(System.in);
                    String message = sc.nextLine();

                    MessageRequestPacket requestPacket = new MessageRequestPacket();
                    requestPacket.setMessage(message);
                    channel.writeAndFlush(PacketCodec.encode(requestPacket, channel.alloc().buffer()));
                }
            }
        }).start();
    }
}
