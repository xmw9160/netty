package com.xmw.code.encode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author xmw.
 * @date 2018/8/20 23:08.
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) {
        // 将Integer转换为 String，并将其添加到 List 中
        out.add(String.valueOf(msg));
    }
}
