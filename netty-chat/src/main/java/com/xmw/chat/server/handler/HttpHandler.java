package com.xmw.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * @author xmw.
 * @date 2018/7/8 17:39.
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String WEB_ROOT = "webroot";
    // classPath
    private URL baseUrl = HttpHandler.class
            .getProtectionDomain()
            .getCodeSource()
            .getLocation();

    private File getFileFromRoot(String fileName) throws Exception {
        String path = baseUrl.toURI() + WEB_ROOT + "/" + fileName;
        // 去掉path的file:前缀
        path = !path.startsWith("file:") ? path : path.substring(5);
        path = path.replaceAll("//", "/");
        return new File(path);
    }

    // netty中只要是方法后面加个0的, 都是实现类的方法, 不是接口
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 获取客户端请求的的uri
        String uri = request.uri();
        String page = uri.equals("/") ? "chat.html" : uri;
        RandomAccessFile accessFile;
        try {
            accessFile = new RandomAccessFile(getFileFromRoot(page), "r");
        } catch (Exception e) {
            ctx.fireChannelRead(request.retain());
            return;
        }
        String contextType = "text/html;";
        if (uri.endsWith(".css")) {
            contextType = "text/css;";
        } else if (uri.endsWith(".js")) {
            contextType = "text/javascript;";
        } else if (uri.toLowerCase().matches("(jpg|png|gif)$")) {
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext + ";";
        }

        // new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contextType + "charset=utf-8;");

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, accessFile.length());
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ctx.write(response);
        ctx.write(new DefaultFileRegion(accessFile.getChannel(), 0, accessFile.length()));

        // 清空缓冲区
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        accessFile.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel client = ctx.channel();
        log.info("Client:" + client.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    // 当一个新的连接已经被建立时，ChannelHandler 的 channelActive()回调方法将会被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
