package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Packet;
import lombok.Data;

import static com.xmw.wechat.protocol.common.Command.LOGOUT_REQUEST;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
