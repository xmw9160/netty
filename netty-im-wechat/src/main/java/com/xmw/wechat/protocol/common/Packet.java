package com.xmw.wechat.protocol.common;

import lombok.Data;

/**
 * 数据传输包对象
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:51
 * @since V1.0
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     */
    public abstract Byte getCommand();
 }
