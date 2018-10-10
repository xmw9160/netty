package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * MessageRequestPacket
 *
 * @author mingwei.xia
 * @date 2018/10/10 14:40
 * @since V1.0
 */
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
