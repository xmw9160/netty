package com.xmw.wechat.client.console;

import java.util.Scanner;

import com.xmw.wechat.protocol.request.QuitGroupRequestPacket;

import io.netty.channel.Channel;

/**
 * 退出群聊命令处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:15
 * @since V1.0
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入需要退出群聊的groupId: ");
        String groupId = scanner.next();

        // 构建退出群聊请求
        QuitGroupRequestPacket requestPacket = new QuitGroupRequestPacket();
        requestPacket.setGroupId(groupId);
        channel.writeAndFlush(requestPacket);
    }
}
