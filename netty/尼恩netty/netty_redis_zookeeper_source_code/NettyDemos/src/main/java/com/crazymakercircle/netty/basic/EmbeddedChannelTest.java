package com.crazymakercircle.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class EmbeddedChannelTest {

    @Test
    public void testLogHandler() {

        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG));

        ByteBuf buf = Unpooled.buffer();

        buf.writeBytes("我是Java高并发卷王，from 疯狂创客圈".getBytes(Charset.forName("utf-8")));

        channel.writeInbound(buf);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


