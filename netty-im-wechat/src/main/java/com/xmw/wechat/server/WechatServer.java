package com.xmw.wechat.server;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.xmw.wechat.codec.PacketDecoder;
import com.xmw.wechat.codec.PacketEncoder;
import com.xmw.wechat.codec.Spliter;
import com.xmw.wechat.server.handler.AuthHandler;
import com.xmw.wechat.server.handler.CreateGroupRequestHandler;
import com.xmw.wechat.server.handler.GroupMessageRequestHandler;
import com.xmw.wechat.server.handler.HeartBeatRequestHandler;
import com.xmw.wechat.server.handler.IMIdleStateHandler;
import com.xmw.wechat.server.handler.JoinGroupRequestHandler;
import com.xmw.wechat.server.handler.LifeCyCleTestHandler;
import com.xmw.wechat.server.handler.ListGroupMembersRequestHandler;
import com.xmw.wechat.server.handler.LoginRequestHandler;
import com.xmw.wechat.server.handler.LogoutRequestHandler;
import com.xmw.wechat.server.handler.MessageRequestHandler;
import com.xmw.wechat.server.handler.QuitGroupRequestHandler;
import com.xmw.wechat.util.ClientCountUtil;

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
@SuppressWarnings("Duplicates")
public class WechatServer {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    // 每次有新连接到来的时候，都会调用ChannelInitializer的initChannel()方法.
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 空闲检测
                        pipeline.addLast(new IMIdleStateHandler());
                        /**
                         * 测试handler生命周期
                         * 为什么要插入到最前面？是因为如果插入到最后面的话，如果这条连接读到了数据，
                         * 但是在 inBound 传播的过程中出错了或者数据处理完完毕就不往后传递了（我们的应用程序属于这类），
                         * 那么最终 IMIdleStateHandler 就不会读到数据，最终导致误判。
                         */
                        pipeline.addLast(new LifeCyCleTestHandler());
                        // 长度域拆包器-过滤自定义协议请求
                        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        pipeline.addLast(new Spliter());
                        // 请求解码
                        pipeline.addLast(new PacketDecoder());
                        // 登录请求处理
                        pipeline.addLast(new LoginRequestHandler());
                        // 心跳处理
                        pipeline.addLast(HeartBeatRequestHandler.INSTANCE);
                        // 认证处理
                        pipeline.addLast(new AuthHandler());
                        // 消息请求处理
                        pipeline.addLast(new MessageRequestHandler());
                        // 创建群聊
                        pipeline.addLast(new CreateGroupRequestHandler());
                        // 进入群聊
                        pipeline.addLast(new JoinGroupRequestHandler());
                        // 退出群聊
                        pipeline.addLast(new QuitGroupRequestHandler());
                        // 群成员
                        pipeline.addLast(new ListGroupMembersRequestHandler());
                        // 群聊天
                        pipeline.addLast(new GroupMessageRequestHandler());
                        // 退出登录
                        pipeline.addLast(new LogoutRequestHandler());
                        // 响应编码
                        pipeline.addLast(new PacketEncoder());
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
