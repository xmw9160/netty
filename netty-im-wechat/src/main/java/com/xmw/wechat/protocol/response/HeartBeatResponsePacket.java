package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

/**
 * @author xmw.
 * @date 2018/10/17 7:33 PM.
 */
public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
