package com.xmw.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author xmw.
 * @date 2018/8/20 23:38.
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    // 传入要使用的SslContext
    // 如果设置为 true，第一个写入的消息将不会被加密（客户端应该设置为 true）
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) {
        // 对于每个SslHandler实例，都使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        // 将SslHandler作为第一个ChannelHandler添加到ChannelPipeline 中
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
