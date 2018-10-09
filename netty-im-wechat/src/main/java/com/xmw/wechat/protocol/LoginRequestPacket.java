package com.xmw.wechat.protocol;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;

import lombok.Data;

/**
 * LoginRequestPacket 登录请求数据包
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:58
 * @since V1.0
 */
@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
