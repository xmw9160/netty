package com.xmw.wechat.protocol.request;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 群聊群员信息查看请求
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:32
 * @since V1.0
 */
@Data
public class ListGroupMembersRequestPacket extends Packet {
    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBER_REQUEST;
    }
}
