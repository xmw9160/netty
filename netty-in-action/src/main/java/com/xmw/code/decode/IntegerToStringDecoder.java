package com.xmw.code.decode;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 22:50.
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) {
        // 将Integer消息转换为它的 String 表示，并将
        out.add(String.valueOf(msg));
    }
}