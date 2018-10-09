package com.xmw.wechat.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.xmw.wechat.serialize.Serializer;
import com.xmw.wechat.serialize.SerializerAlgorithm;

/**
 * JsonSerializer
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:45
 * @since V1.0
 */
public class JsonSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
