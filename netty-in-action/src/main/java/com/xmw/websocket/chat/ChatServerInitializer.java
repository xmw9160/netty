package com.xmw.websocket.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author xmw.
 * @date 2018/8/22 22:47.
 */
// 扩展了 ChannelInitializer
public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 将所有需要的ChannelHandler添加到ChannelPipeline中
        ChannelPipeline pipeline = ch.pipeline();
        // 将字节解码为 HttpRequest 、 HttpContent 和 LastHttp-
        // Content 。并将 HttpRequest 、 HttpContent 和 Last-
        // HttpContent 编码为字节
        pipeline.addLast(new HttpServerCodec());
        // 写入一个文件的内容
        pipeline.addLast(new ChunkedWriteHandler());
        // 将一个 HttpMessage 和跟随它的多个 HttpContent 聚合
        // 为单个 FullHttpRequest 或者 FullHttpResponse （取
        // 决于它是被用来处理请求还是响应）。安装了这个之后，
        // ChannelPipeline 中的下一个 ChannelHandler 将只会
        // 收到完整的 HTTP 请求或响应
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理FullHttpRequest（那些不发送到 /ws URI 的请求）
        pipeline.addLast(new HttpRequestHandler("/ws"));
        // 按照WebSocket规范的要求，处理WebSocket升级握手、PingWebSocketFrame、PongWebSocketFrame
        // 和CloseWebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 处理websocket帧数据
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
