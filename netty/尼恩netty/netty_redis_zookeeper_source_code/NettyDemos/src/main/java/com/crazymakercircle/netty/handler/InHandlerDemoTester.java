package com.crazymakercircle.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class InHandlerDemoTester {


    @Test
    public void testInHandlerLifeCircle() {

        final InHandlerDemo inHandler = new InHandlerDemo();

        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(inHandler);


//        channel.pipeline().addLast("inhandler",inHandler);
//        channel.pipeline().remove("inhandler");
//        channel.pipeline().remove(inHandler);


        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testInHandlerProcess() {

        final InHandlerDemo inHandler = new InHandlerDemo();

        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(inHandler,new LoggingHandler(LogLevel.DEBUG));

        ByteBuf buf = Unpooled.buffer();
         buf.writeInt(1);

        //模拟入站，写一个入站包
        channel.writeInbound(buf);
        channel.flush();

        //通道关闭
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInHandlerProcessWithChannelInitializer() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        //初始化处理器
        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //模拟入站，写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //模拟入站，再写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //通道关闭
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSharable() {

        final InHandlerDemo inHandler = new InHandlerDemo();
        //初始化处理器
        ChannelInitializer initializer = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);
                ch.pipeline().addLast(inHandler);
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //模拟入站，写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        channel.close();

        System.out.println(" 再来一个 channel" );
        EmbeddedChannel channel2 = new EmbeddedChannel(initializer);

        //模拟入站，再写一个入站包
        channel2.writeInbound(buf);
        channel2.flush();
        //通道关闭
        channel2.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
