package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 退出群聊响应数据包
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:13
 * @since V1.0
 */
@Data
public class QuitGroupResponsePacket extends Packet {
    private boolean isSuccess;
    private String groupId;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
