package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;
import lombok.Data;

import java.util.List;

/**
 * 创建群聊数据包
 *
 * @author mingwei.xia
 * @date 2018/10/16 11:13
 * @since V1.0
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
