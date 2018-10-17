package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 群聊消息请求包
 *
 * @author mingwei.xia
 * @date 2018/10/17 12:48
 * @since V1.0
 */
@Data
public class GroupMessageRequestPacket extends Packet {
    private String groupId;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
