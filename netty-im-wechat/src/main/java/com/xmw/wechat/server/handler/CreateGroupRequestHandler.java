package com.xmw.wechat.server.handler;

import java.util.ArrayList;
import java.util.List;

import com.xmw.wechat.protocol.request.CreateGroupRequestPacket;
import com.xmw.wechat.protocol.response.CreategroupResponsePacket;
import com.xmw.wechat.util.IDUtil;
import com.xmw.wechat.util.SessionUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

/**
 * 创建群聊处理
 *
 * @author mingwei.xia
 * @date 2018/10/16 11:25
 * @since V1.0
 */
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) {
        List<String> userIdList = msg.getUserIdList();
        List<String> userNameList = new ArrayList<>();
        //1.创建一个channel分组
        /**
         * 它可以把多个 chanel 的操作聚合在一起，可以往它里面添加删除 channel，
         * 可以进行 channel 的批量读写，关闭等操作.
         */
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        //2.筛选出待加入群聊的用户的 channel 和 userName
        for (String userId : userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }
        //3.创建群聊创建结果的响应
        CreategroupResponsePacket responsePacket = new CreategroupResponsePacket();
        responsePacket.setIsSuccess(true);
        responsePacket.setGroupId(IDUtil.randomId());
        responsePacket.setUserNameList(userNameList);

        //4.给每个客户端发送拉群通知
        channelGroup.writeAndFlush(responsePacket);

        //5.保存群聊信息
        SessionUtil.addChannelGroup(responsePacket.getGroupId(), channelGroup);

        System.out.print("群创建成功，id 为[" + responsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + responsePacket.getUserNameList());
    }
}
