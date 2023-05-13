package com.crazymakercircle.netty.protocol;

import com.crazymakercircle.util.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//服务器端业务处理器
public class JsonMsgDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String json = (String) msg;

        JsonMsg jsonMsg = JsonMsg.parseFromJson(json);

        Logger.info("收到一个 Json 数据包 =》" + jsonMsg);

        ctx.fireChannelRead(jsonMsg);

    }
}