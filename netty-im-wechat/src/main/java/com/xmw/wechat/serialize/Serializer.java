package com.xmw.wechat.serialize;

import com.xmw.wechat.serialize.impl.JsonSerializer;

/**
 * Serializer 序列化接口
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:42
 * @since V1.0
 */
public interface Serializer {

    /**
     * json 序列化
     */
    byte JSON_SERIALIZER = 1;

    /**
     * 默认序列化方式
     */
    Serializer DEFAULT = new JsonSerializer();

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
