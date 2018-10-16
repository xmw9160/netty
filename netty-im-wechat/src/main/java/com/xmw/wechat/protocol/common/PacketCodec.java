package com.xmw.wechat.protocol.common;

import com.xmw.wechat.protocol.request.CreateGroupRequestPacket;
import com.xmw.wechat.protocol.request.LoginRequestPacket;
import com.xmw.wechat.protocol.request.LogoutRequestPacket;
import com.xmw.wechat.protocol.request.MessageRequestPacket;
import com.xmw.wechat.protocol.response.CreategroupResponsePacket;
import com.xmw.wechat.protocol.response.LoginResponsePacket;
import com.xmw.wechat.protocol.response.LogoutResponsePacket;
import com.xmw.wechat.protocol.response.MessageResponsePacket;
import com.xmw.wechat.serialize.Serializer;
import com.xmw.wechat.serialize.impl.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.xmw.wechat.protocol.common.Command.CREATE_GROUP_REQUEST;
import static com.xmw.wechat.protocol.common.Command.CREATE_GROUP_RESPONSE;
import static com.xmw.wechat.protocol.common.Command.LOGIN_REQUEST;
import static com.xmw.wechat.protocol.common.Command.LOGIN_RESPONSE;
import static com.xmw.wechat.protocol.common.Command.LOGOUT_REQUEST;
import static com.xmw.wechat.protocol.common.Command.LOGOUT_RESPONSE;
import static com.xmw.wechat.protocol.common.Command.MESSAGE_REQUEST;
import static com.xmw.wechat.protocol.common.Command.MESSAGE_RESPONSE;

/**
 * 编解码工具类
 *
 * @author mingwei.xia
 * @date 2018/10/9 16:59
 * @since V1.0
 */
public class PacketCodec {
    public static final int MAGIC_NUMBER = 0x9160;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreategroupResponsePacket.class);
        packetTypeMap.put(LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(LOGOUT_RESPONSE, LogoutResponsePacket.class);


        serializerMap = new HashMap<>();
        Serializer serializer = new JsonSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    private PacketCodec() {
    }

    /**
     * 编码数据包对象
     *
     * @param packet  数据包对象
     * @param byteBuf 返回数据
     * @return 封装好的数据包
     * @author mingwei.xia
     * @date 2018/10/9 17:01
     */
    public static ByteBuf encode(Packet packet, ByteBuf byteBuf) {
        if (byteBuf == null) {
            byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        }
        //1. 魔数(4byte)
        byteBuf.writeInt(MAGIC_NUMBER);
        //2. 版本号(1byte)
        byteBuf.writeByte(packet.getVersion());
        //3. 序列化方式(1byte): json
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //4. 指令(1byte)
        byteBuf.writeByte(packet.getCommand());
        //5. 数据长度(4byte)
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        byteBuf.writeInt(bytes.length);
        //6. 数据内容
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    /**
     * 对数据包进行解码
     *
     * @param byteBuf 数据包
     * @return 反序列化后的对象
     * @author mingwei.xia
     * @date 2018/10/9 17:14
     */
    public static Packet decode(ByteBuf byteBuf) {
//        //1. 魔数(4byte)
//        int magicNum = byteBuf.getInt(0);
//        //2. 版本号(1byte)
//        byte version = byteBuf.getByte(4);
//        //3. 序列化方式(1byte): json
//        byte serializerType = byteBuf.getByte(5);
//        //4. 指令(1byte)
//        byte command = byteBuf.getByte(6);
//        //5. 数据长度(4byte)
//        int dataLength = byteBuf.getInt(7);
//        //6. 数据内容
//        byte[] data = new byte[dataLength];
//        byteBuf.readBytes(data);
//        return Serializer.DEFAULT.deserialize(data, clazz);

        // 跳过 magic number
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        // 数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        // 反序列化
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(bytes, requestType);
        }

        return null;
    }

    private static Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private static Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
