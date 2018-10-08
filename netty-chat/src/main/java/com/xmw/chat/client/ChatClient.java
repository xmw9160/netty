package com.xmw.chat.client;

import com.xmw.chat.client.handler.ChatClientHandler;
import com.xmw.chat.protocol.IMDecoder;
import com.xmw.chat.protocol.IMEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author xmw.
 * @date 2018/7/11 23:46.
 */
public class ChatClient {
    private ChatClientHandler clientHandler;

    public ChatClient(String nickName) {
        this.clientHandler = new ChatClientHandler(nickName);
    }

    public static void main(String[] args) {
        new ChatClient("Sam").connect("127.0.0.1", 80);
    }

    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new IMDecoder());
                    ch.pipeline().addLast(new IMEncoder());
                    ch.pipeline().addLast(clientHandler);
                }
            });
            // 异步地连接到远程节点
            // 这里，connect()方法将会直接返回，而不会阻塞，该调用将会在后台完成
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
