package com.xmw.wechat.server.handler;

import com.xmw.wechat.util.ClientCountUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * LifeCyCleTestHandler
 *
 * @author mingwei.xia
 * @date 2018/10/11 15:06
 * @since V1.0
 */
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {

    /**
     * handlerAdded() -> channelRegistered() -> channelActive() -> channelRead() -> channelReadComplete()
     */

    /**
     * handlerAdded() ：指的是当检测到新连接之后，调用 ch.pipeline().addLast(new LifeCyCleTestHandler());
     * 之后的回调，表示在当前的 channel 中，已经成功添加了一个 handler 处理器
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被添加：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * channelRegistered()：这个回调方法，表示当前的 channel 的所有的逻辑处理已经和某个 NIO 线程建立了绑定关系，
     * 类似我们在Netty 是什么？这小节中 BIO 编程中，accept 到新的连接，然后创建一个线程来处理这条连接的读写，
     * 只不过 Netty 里面是使用了线程池的方式，只需要从线程池里面去抓一个线程绑定在这个 channel 上即可，
     * 这里的 NIO 线程通常指的是 NioEventLoop.
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 绑定到线程(NioEventLoop)：channelRegistered()");
        super.channelRegistered(ctx);
    }

    /**
     * 当 channel 的所有的业务逻辑链准备完毕（也就是说 channel 的 pipeline 中已经添加完所有的 handler）
     * 以及绑定好一个 NIO 线程之后，这条连接算是真正激活了，接下来就会回调到此方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加连接客户端数
        ClientCountUtil.addClient();

        System.out.println("channel 准备就绪：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 客户端向服务端发来数据，每次都会回调此方法，表示有数据可读。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 服务端每次读完一次完整的数据之后，回调该方法，表示数据读取完毕。
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 某次数据读完：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    /**
     * channelInactive() -> channelUnregistered() -> handlerRemoved()
     */

    /**
     * 表面这条连接已经被关闭了，这条连接在 TCP 层面已经不再是 ESTABLISH 状态了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 减少连接客户端数
        ClientCountUtil.subClient();

        System.out.println("channel 被关闭：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     * 既然连接已经被关闭，那么与这条连接绑定的线程就不需要对这条连接负责了，
     * 这个回调就表明与这条连接对应的 NIO 线程移除掉对这条连接的处理
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    /**
     * 最后，我们给这条连接上添加的所有的业务逻辑处理器都给移除掉。
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
