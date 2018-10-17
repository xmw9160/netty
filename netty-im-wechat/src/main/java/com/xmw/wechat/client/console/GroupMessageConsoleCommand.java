package com.xmw.wechat.client.console;

import java.util.Scanner;

import com.xmw.wechat.protocol.request.GroupMessageRequestPacket;

import io.netty.channel.Channel;

/**
 * 群聊
 *
 * @author mingwei.xia
 * @date 2018/10/17 12:44
 * @since V1.0
 */
public class GroupMessageConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入发送信息, 格式: groupId message");
        String groupId = scanner.next();
        String message = scanner.next();

        // 发送消息
        GroupMessageRequestPacket requestPacket = new GroupMessageRequestPacket();
        requestPacket.setGroupId(groupId);
        requestPacket.setMessage(message);
        channel.writeAndFlush(requestPacket);
    }
}
