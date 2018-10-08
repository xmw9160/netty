package com.xmw.chat.server;

import com.xmw.chat.protocol.IMDecoder;
import com.xmw.chat.protocol.IMEncoder;
import com.xmw.chat.server.handler.HttpHandler;
import com.xmw.chat.server.handler.SocketHandler;
import com.xmw.chat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xmw.
 * @date 2018/7/8 17:28.
 */
@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        try {
            new ChatServer().start(80);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) {
        // boss 线程
        EventLoopGroup boss = new NioEventLoopGroup();
        // worker 线程
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 启动引擎
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new ChannelHandler() {
                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) {

                        }

                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) {

                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

                        }
                    }) // 做权限校验
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 自定业务处理
                            ChannelPipeline pipeline = ch.pipeline();
                            // ==================Http 协议 =================================
                            // 解码和编码HTTP请求
                            // 将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            // 主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的
                            // 加上这个handler我们就不用考虑这个问题了
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpHandler());
                            // ==================Http 协议 =================================

                            // ==================WebSocket 协议 =================================
                            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                            pipeline.addLast(new WebSocketHandler());
                            // ==================WebSocket 协议 =================================

                            // ==================自定义socket协议 =================================
                            pipeline.addLast(new IMDecoder());
                            pipeline.addLast(new IMEncoder());
                            pipeline.addLast(new SocketHandler());
                            // ==================自定义socket协议 =================================

                        }
                    });

            // 等待客户端连接
            ChannelFuture sync = server.bind(port).sync();
            log.info("服务已启动, 监听端口: " + port);
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
