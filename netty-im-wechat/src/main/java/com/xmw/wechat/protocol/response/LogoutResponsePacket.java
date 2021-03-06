package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Packet;

import static com.xmw.wechat.protocol.common.Command.LOGOUT_RESPONSE;
import lombok.Data;

@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}
