package com.xmw.catalina.netty.server;

import com.xmw.catalina.http.XmwRequest;
import com.xmw.catalina.http.XmwResponse;
import com.xmw.catalina.servlets.MyServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author xmw.
 * @date 2018/7/8 10:40.
 */
public class XmwTomcatHandler extends ChannelInboundHandlerAdapter {

    // 一次请求为什么会调用4次此方法
    // 当所有可读的字节都已经从 Channel 中读取之后，将会调用该回调方法；
    // 所以，可能在channelReadComplete()被调用之前看到多次调用 channelRead(...)
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        long start = System.currentTimeMillis();
        if (msg instanceof HttpRequest) {
            HttpRequest r = (HttpRequest) msg;
            XmwRequest request = new XmwRequest(ctx, r);
            XmwResponse response = new XmwResponse(ctx, r);

            MyServlet myServlet = MyServlet.class.newInstance();
            myServlet.doGet(request, response);
            System.out.println("执行业务逻辑....");
        }
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + " ms");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
