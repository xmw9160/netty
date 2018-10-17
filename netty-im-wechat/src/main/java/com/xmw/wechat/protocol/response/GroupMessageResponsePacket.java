package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 群聊消息响应
 *
 * @author mingwei.xia
 * @date 2018/10/17 12:49
 * @since V1.0
 */
@Data
public class GroupMessageResponsePacket extends Packet {
    private String groupId;
    private String fromUserId;
    private String fromUserName;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
