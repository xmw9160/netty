package com.xmw.wechat.client.console;

import java.util.Arrays;
import java.util.Scanner;

import com.xmw.wechat.protocol.request.CreateGroupRequestPacket;

import io.netty.channel.Channel;

/**
 * 创建群聊指令处理
 *
 * @author mingwei.xia
 * @date 2018/10/16 11:11
 * @since V1.0
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {
    private static final String USER_ID_SPLITTER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();

        System.out.println("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        String userIds = scanner.next();
        createGroupRequestPacket.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLITTER)));
        channel.writeAndFlush(createGroupRequestPacket);
    }
}
