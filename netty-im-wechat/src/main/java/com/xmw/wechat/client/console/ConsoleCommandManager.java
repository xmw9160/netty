package com.xmw.wechat.client.console;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 命令控制管理
 *
 * @author mingwei.xia
 * @date 2018/10/16 11:05
 * @since V1.0
 */
public class ConsoleCommandManager implements ConsoleCommand {
    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        this.consoleCommandMap = new HashMap<>();
        this.consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        this.consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        this.consoleCommandMap.put("logout", new LogoutConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        // 获取第一个指令
        String command = scanner.next();
        // 获取指令对应的命令处理器
        ConsoleCommand consoleCommand = this.consoleCommandMap.get(command);
        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
    }
}
