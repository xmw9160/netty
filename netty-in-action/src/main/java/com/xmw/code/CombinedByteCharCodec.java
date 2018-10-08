package com.xmw.code;

import com.xmw.code.decode.ByteToCharDecoder;
import com.xmw.code.encode.CharToByteEncoder;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @author xmw.
 * @date 2018/8/20 23:26.
 */
// 通过该解码器和编码器实现参数化 CombinedByteCharCodec
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        // 将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
