package com.xmw.websocket.chat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * @author xmw.
 * @date 2018/8/22 23:12.
 */
// 扩展 ChatServerInitializer以添加加密
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SslContext context;
    public SecureChatServerInitializer(ChannelGroup group,
                                       SslContext context) {
        super(group);
        this.context = context;
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        // 调用父类的initChannel()方法
//        SSLEng.ine engine = context.newEngine(ch.alloc());
//        engine.setUseClientMode(false);
        // 将 SslHandler 添加到ChannelPipeline 中
//        ch.pipeline().addFirst(new SslHandler(engine));
    }
}