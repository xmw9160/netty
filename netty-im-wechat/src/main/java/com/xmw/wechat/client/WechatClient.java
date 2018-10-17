package com.xmw.wechat.client;

import java.util.Scanner;

import com.xmw.wechat.client.console.ConsoleCommandManager;
import com.xmw.wechat.client.handler.CreateGroupResponseHandler;
import com.xmw.wechat.client.handler.GroupMessageResponseHandler;
import com.xmw.wechat.client.handler.JoinGroupResponseHandler;
import com.xmw.wechat.client.handler.ListGroupMembersResponseHandler;
import com.xmw.wechat.client.handler.LoginResponseHandler;
import com.xmw.wechat.client.handler.LogoutResponseHandler;
import com.xmw.wechat.client.handler.MessageResponseHandler;
import com.xmw.wechat.client.handler.QuitGroupResponseHandler;
import com.xmw.wechat.codec.PacketDecoder;
import com.xmw.wechat.codec.PacketEncoder;
import com.xmw.wechat.codec.Spliter;
import com.xmw.wechat.server.handler.IMIdleStateHandler;
import com.xmw.wechat.util.SessionUtil;

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
                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        // 长度域拆包器
                        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new Spliter());
                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(new LoginResponseHandler());
                        pipeline.addLast(new MessageResponseHandler());
                        pipeline.addLast(new CreateGroupResponseHandler());
                        pipeline.addLast(new JoinGroupResponseHandler());
                        pipeline.addLast(new QuitGroupResponseHandler());
                        pipeline.addLast(new ListGroupMembersResponseHandler());
                        pipeline.addLast(new GroupMessageResponseHandler());
                        pipeline.addLast(new LogoutResponseHandler());
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
        Scanner sc = new Scanner(System.in);
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        // LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        // LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                // 请求登录
                if (!SessionUtil.hasLogin(channel)) {
//                     System.out.print("客户端发送消息: ");
//                    System.out.println("请输入用户名");
//                    String userName = sc.nextLine();
//                    loginRequestPacket.setUsername(userName);
//                    loginRequestPacket.setPassword("8888");
//                    channel.writeAndFlush(loginRequestPacket);
//                    waitForLoginResponse();
                    // 登录操作
                    //loginConsoleCommand.exec(sc, channel);
                } else {
                    // 登录成功, 发送消息
//                    String toUserId = sc.next();
//                    String message = sc.next();
//                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                    consoleCommandManager.exec(sc, channel);
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
