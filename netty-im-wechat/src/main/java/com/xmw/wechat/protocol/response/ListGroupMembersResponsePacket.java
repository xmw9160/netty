package com.xmw.wechat.protocol.response;

import java.util.List;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;
import com.xmw.wechat.session.Session;

import lombok.Data;

/**
 * 群聊群员信息查看响应
 *
 * @author mingwei.xia
 * @date 2018/10/17 10:35
 * @since V1.0
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {
    private String groupId;
    private List<Session> sessionList;
    private String message;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBER_RESPONSE;
    }
}
