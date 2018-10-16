package com.xmw.wechat.protocol.response;

import java.util.List;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * 响应创建群聊数据包
 *
 * @author mingwei.xia
 * @date 2018/10/16 13:52
 * @since V1.0
 */
@Data
public class CreategroupResponsePacket extends Packet {
    private Boolean isSuccess;
    private String groupId;
    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
