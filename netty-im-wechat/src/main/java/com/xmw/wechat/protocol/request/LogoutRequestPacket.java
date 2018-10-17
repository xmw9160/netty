package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Packet;

import static com.xmw.wechat.protocol.common.Command.LOGOUT_REQUEST;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
