package com.xmw.wechat.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台命令抽象类
 *
 * @author mingwei.xia
 * @date 2018/10/16 10:59
 * @since V1.0
 */
public interface ConsoleCommand {

    /**
     * 命令执行
     *
     * @param scanner scanner
     * @param channel channel
     * @author mingwei.xia
     * @date 2018/10/16 11:00
     */
    void exec(Scanner scanner, Channel channel);
}
