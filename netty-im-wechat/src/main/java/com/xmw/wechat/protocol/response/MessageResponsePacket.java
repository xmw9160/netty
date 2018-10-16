package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;
import lombok.Data;

/**
 * MessageResponsePacket
 *
 * @author mingwei.xia
 * @date 2018/10/10 14:40
 * @since V1.0
 */
@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
