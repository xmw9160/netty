package com.xmw.chat.process;

import com.alibaba.fastjson.JSONObject;
import com.xmw.chat.protocol.IMDecoder;
import com.xmw.chat.protocol.IMEncoder;
import com.xmw.chat.protocol.IMMessage;
import com.xmw.chat.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xmw.
 * @date 2018/7/10 22:59.
 */
@Slf4j
public class IMProcessor {

    private final static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final static String LAST_FLOWER_TIME = "lastFlowerTime";
    private final AttributeKey<String> NICK_NAME = AttributeKey.valueOf("nickName");
    private final AttributeKey<String> IP_ADDR = AttributeKey.valueOf("ipAddr");
    private final AttributeKey<JSONObject> ATTRS = AttributeKey.valueOf("attrs");
    private IMDecoder decoder = new IMDecoder();
    private IMEncoder encoder = new IMEncoder();

    public void logout(Channel client) {
        onlineUsers.remove(client);
    }

    public void process(Channel client, IMMessage msg) {
        process(client, encoder.encode(msg));
    }

    public void process(Channel client, String msg) {
        log.info(msg);
        IMMessage request = decoder.decode(msg);
        if (request == null) {
            return;
        }

        String nickName = request.getSender();

        // 登录动作, 在onlineUsers中加入一条信息
        if (IMP.LOGIN.getName().equals(request.getCmd())) {
            // 保存昵称属性
            client.attr(NICK_NAME).getAndSet(request.getSender());
            onlineUsers.add(client);

            // 循环客户端, 通知所有人
            for (Channel channel : onlineUsers) {
                if (channel != client) {
                    request = new IMMessage(IMP.SYSTEM.getName(), systemTime(), onlineUsers.size(), nickName + "加入聊天室");
                } else {
                    request = new IMMessage(IMP.SYSTEM.getName(), systemTime(), onlineUsers.size(), "连接服务器成功");
                }
                channel.writeAndFlush(new TextWebSocketFrame(encoder.encode(request)));
            }

        } else if (IMP.LOGOUT.getName().equals(request.getCmd())) {
            onlineUsers.remove(client);
        } else if (IMP.CHAT.getName().equals(request.getCmd())) {
            // 循环客户端, 通知所有人
            for (Channel channel : onlineUsers) {
                if (channel != client) {
                    request.setSender(client.attr(NICK_NAME).get());
                } else {
                    request.setSender("you");
                }
                channel.writeAndFlush(new TextWebSocketFrame(encoder.encode(request)));
            }
        } else if (IMP.FLOWER.getName().equals(request.getCmd())) {
            // 客户端发送送鲜花的命令过来
            JSONObject attrs = getAttrs(client);
            Long currentTime = systemTime();
            if (attrs != null) {
                long lastTime = attrs.getLongValue(LAST_FLOWER_TIME);
                // 10s内不允许重复刷鲜花
                int seconds = 10;
                long sub = currentTime - lastTime;
                if (sub < 1000 * seconds) {
                    request.setSender("you");
                    request.setCmd("您送的鲜花太频繁, " + (seconds - Math.round(sub / 1000)) + "秒后重试.");
                    String content = encoder.encode(request);
                    client.writeAndFlush(content);
                    return;
                }
            }

            // 正常送鲜花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setContent("您送了大家一波鲜花");
                    // 设置最后一次送鲜花的时间
                    setAttrs(client, LAST_FLOWER_TIME, currentTime);
                } else {
                    request.setSender(client.attr(NICK_NAME).get());
                    request.setContent(client.attr(NICK_NAME).get() + "送来一波鲜花");
                }
                request.setTime(systemTime());

                String flower = encoder.encode(request);
                log.info("flower : {}", flower);
                channel.writeAndFlush(flower);
            }
        }
    }

    private Long systemTime() {
        return System.currentTimeMillis();
    }

    private JSONObject getAttrs(Channel client) {
        return client.attr(ATTRS).get();
    }

    private void setAttrs(Channel client, String key, Object value) {
        JSONObject json = client.attr(ATTRS).get();
        if (json != null) {
            json.put(key, value);
            client.attr(ATTRS).set(json);
        } else {
            json = new JSONObject();
            json.put(key, value);
            client.attr(ATTRS).set(json);
        }
    }
}
