package com.xmw.wechat.client.console;

import java.util.Scanner;

import com.xmw.wechat.protocol.request.ListGroupMembersRequestPacket;

import io.netty.channel.Channel;

/**
 * 群聊群员信息查看
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:29
 * @since V1.0
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入需要查看群聊的groupId: ");
        String groupId = scanner.next();
        // 构建请求
        ListGroupMembersRequestPacket requestPacket = new ListGroupMembersRequestPacket();
        requestPacket.setGroupId(groupId);
        channel.writeAndFlush(requestPacket);
    }
}
