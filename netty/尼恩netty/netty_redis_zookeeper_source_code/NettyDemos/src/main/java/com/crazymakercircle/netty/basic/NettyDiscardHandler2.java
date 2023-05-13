package com.crazymakercircle.netty.basic;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class NettyDiscardHandler2 extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Logger.info("收到消息,丢弃如下:");
        ByteBuf in = (ByteBuf) msg;
        while (in.isReadable()) {
            System.out.print((char) in.readByte());
        }
        System.out.println();
    }
}
