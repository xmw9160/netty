package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 加入群聊数据包
 *
 * @author mingwei.xia
 * @date 2018/10/17 9:20
 * @since V1.0
 */
@Data
public class JoinGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
