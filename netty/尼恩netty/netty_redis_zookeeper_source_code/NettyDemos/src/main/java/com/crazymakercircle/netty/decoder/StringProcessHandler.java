package com.crazymakercircle.netty.decoder;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StringProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof  String) {
            String s = (String) msg;
            System.out.println("打印: " + s);
        }else {
            super.channelRead(ctx,msg);
        }

//        super.channelRead(ctx,msg);
//        ctx.fireChannelRead(msg);

    }
}