package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 加入群聊响应数据包
 *
 * @author mingwei.xia
 * @date 2018/10/17 9:41
 * @since V1.0
 */
@Data
public class JoinGroupResponsePacket extends Packet {
    private boolean isSuccess;
    private String groupId;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
