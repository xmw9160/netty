package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

/**
 * 心跳检查数据包
 *
 * @author mingwei.xia
 * @date 2018/10/17 17:40
 * @since V1.0
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
