package com.xmw.wechat.protocol.response;

import com.xmw.wechat.protocol.common.Command;
import com.xmw.wechat.protocol.common.Packet;
import lombok.Data;

/**
 * LoginResponsePacket
 *
 * @author mingwei.xia
 * @date 2018/10/10 13:58
 * @since V1.0
 */
@Data
public class LoginResponsePacket extends Packet {
    /**
     * 是否登录成功
     */
    private Boolean isSuccess;

    /**
     * 登录用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 理由
     */
    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
