package com.crazymakercircle.netty.protocol;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class JsonMsgEncoder extends MessageToMessageEncoder<JsonMsg> {



    @Override
    protected void encode(ChannelHandlerContext ctx, JsonMsg user, List<Object> out) throws Exception {

        // 原始数据
        String json = user.convertToJson();
        Logger.info("发送报文：" + json);
        out.add(json);

    }
}
