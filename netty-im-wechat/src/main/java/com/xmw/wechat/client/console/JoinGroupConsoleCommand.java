package com.xmw.wechat.client.console;

import java.util.Scanner;

import com.xmw.wechat.protocol.request.JoinGroupRequestPacket;

import io.netty.channel.Channel;

/**
 * 加入群聊命令处理
 *
 * @author mingwei.xia
 * @date 2018/10/17 9:19
 * @since V1.0
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinGroupRequestPacket requestPacket = new JoinGroupRequestPacket();

        System.out.println("请输入groupId, 进入群聊: ");
        String groupId = scanner.next();

        requestPacket.setGroupId(groupId);
        channel.writeAndFlush(requestPacket);
    }
}
