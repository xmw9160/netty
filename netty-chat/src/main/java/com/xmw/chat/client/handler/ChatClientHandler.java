package com.xmw.chat.client.handler;

import com.xmw.chat.protocol.IMMessage;
import com.xmw.chat.protocol.IMP;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 聊天客户端逻辑实现
 */
@Slf4j
public class ChatClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;
    private String nickName;

    public ChatClientHandler(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 启动客户端控制台
     */
    private void session() {
        new Thread(() -> {
            log.info(nickName + ",你好，请在控制台输入消息内容");
            IMMessage message = null;
            Scanner scanner = new Scanner(System.in);
            do {
                if (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    if ("exit".equals(input)) {
                        message = new IMMessage(IMP.LOGOUT.getName(), System.currentTimeMillis(), nickName);
                    } else {
                        message = new IMMessage(IMP.CHAT.getName(), System.currentTimeMillis(), nickName, input);
                    }
                }
            }
            while (sendMsg(message));
            scanner.close();
        }).start();
    }

    /**
     * tcp链路建立成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        IMMessage message = new IMMessage(IMP.LOGIN.getName(), System.currentTimeMillis(), this.nickName);
        sendMsg(message);
        log.info("成功连接服务器,已执行登录动作");
        session();
    }

    /**
     * 发送消息
     */
    private boolean sendMsg(IMMessage msg) {
        ctx.channel().writeAndFlush(msg);
        log.info("已发送至聊天面板,请继续输入");
        return !IMP.LOGOUT.getName().equals(msg.getCmd());
    }

    /**
     * 收到消息后调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        IMMessage m = (IMMessage) msg;
        log.info(m.toString());
    }

    /**
     * 发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("与服务器断开连接:" + cause.getMessage());
        ctx.close();
    }
}
