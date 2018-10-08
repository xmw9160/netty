package com.xmw.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author xmw.
 * @date 2018/8/21 23:19.
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // 将 SslHandler 添加到ChannelPipeline 中以使用 HTTPS
        SSLEngine engine = context.newEngine(ch.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));
        if (isClient) {
            // 如果是客户端，则添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 如果是服务器，则添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}