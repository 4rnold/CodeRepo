package com.crazymakercircle.imClient.handler;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.im.common.protoBuilder.NotificationMsgBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@ChannelHandler.Sharable
@Service("soulTest1ClientHandler")
public class SoulTest1ClientHandler extends ChannelInboundHandlerAdapter {
    //心跳的时间间隔，单位为s
    private static final int HEARTBEAT_INTERVAL = 50;

    //在Handler被加入到Pipeline时，开始发送心跳
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        ProtoMsg.Message message =
                NotificationMsgBuilder.buildNotification("疯狂创客圈 Netty 灵魂实验 1 " + ctx.channel().id());
        //发送心跳报文
        clientHeartBeat(ctx, message);
    }

    //使用定时器，发送心跳报文
    public void clientHeartBeat(ChannelHandlerContext ctx,
                                ProtoMsg.Message  heartbeatMsg) {
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                log.info(" 发送 HEART_BEAT  消息 to server");
                ctx.writeAndFlush(heartbeatMsg);

                //递归调用，发送下一次的心跳
                clientHeartBeat(ctx, heartbeatMsg);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
    /**
     * 接受到服务器的心跳回写
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //处理消息的内容
        ProtoMsg.Message notificationPkg = (ProtoMsg.Message) msg;

        String json = notificationPkg.getNotification().getJson();

        log.info("{} 服务端的回复： {}", ctx.channel().id(), json);

    }

}
