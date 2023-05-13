package com.crazymakercircle.netty.pipeline;

import com.crazymakercircle.netty.handler.OutHandlerDemo;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class InPipeline {
    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Logger.info("入站处理器 A: 被回调 ");

//            super.channelReadComplete(ctx);
            ctx.fireChannelReadComplete(); //入站操作的传播
        }


    }
    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 B: 被回调 ");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Logger.info("入站处理器 B: 被回调 ");
            ctx.fireChannelReadComplete();//入站操作的传播
        }
    }


    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 C: 被回调 ");
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Logger.info("入站处理器 C: 被回调 ");
            ctx.fireChannelReadComplete();//入站操作的传播
        }
    }


    @Test
    public void testPipelineInBound() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //向通道写一个入站报文
        channel.writeInbound(buf);
        ThreadUtil.sleepSeconds(Integer.MAX_VALUE);
    }


    static class SimpleInHandlerB2 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 B2: 被回调 ");
            //不调用基类的channelRead, 终止流水线的执行
//            super.channelRead(ctx, msg);
     //  ctx.fireChannelRead(msg);
        }



        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Logger.info("入站处理器 B2: 被回调 ");
            ctx.fireChannelReadComplete();//入站操作的传播
        }
    }

    //测试流水线的截断
    @Test
    public void testPipelineCutting() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB2());
                ch.pipeline().addLast(new SimpleInHandlerC());

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //向通道写一个入站报文
        channel.writeInbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    static class SimpleInHandlerB3 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Logger.info("入站处理器 B2: 被回调 ");


            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(10);

//            ctx.channel().writeAndFlush(buf);

//          ctx.pipeline().writeAndFlush(buf);

            ctx.writeAndFlush(buf);

            //不调用基类的channelRead, 终止流水线的执行
//            super.channelRead(ctx, msg);
            //  ctx.fireChannelRead(msg);
            //
            //
                }

}

    //测试不同输出方式，不同出站方式
    @Test
    public void testMultiplyOutput() {


        OutHandlerDemo  outHandlerDemo=new OutHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(outHandlerDemo);
                ch.pipeline().addLast(new SimpleInHandlerB3());
                ch.pipeline().addLast(new SimpleInHandlerC());
                ch.pipeline().addLast(outHandlerDemo);

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //向通道写一个入站报文
        channel.writeInbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
