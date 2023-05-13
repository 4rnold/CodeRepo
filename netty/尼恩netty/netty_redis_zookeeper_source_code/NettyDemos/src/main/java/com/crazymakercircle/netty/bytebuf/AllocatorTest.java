package com.crazymakercircle.netty.bytebuf;

import com.crazymakercircle.util.JvmUtil;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;


public class AllocatorTest {

    //Xmx1000m -Xms1000m
    @Test
    public void showUnpooledByteBufAllocator() {
        UnpooledByteBufAllocator allocator = UnpooledByteBufAllocator.DEFAULT;
        Logger.tcfo(JvmUtil.getMxMemory());

        for (int i = 0; i < 1000; i++) {
            ByteBuf buffer = allocator.directBuffer(20 * 1024 * 1024);
//            buffer.release();
            Logger.tcfo(buffer);
            System.out.println("分配了 " + 20 * (i + 1) + " MB");
        }

//        for (int i = 0; i < 1000; i++) {
//            ByteBuf buffer = allocator.heapBuffer(20 * 1024 * 1024);
////            buffer.release();
//            Logger.tcfo(buffer);
//            System.out.println("分配了 " + 20 * (i + 1) + " MB");
//        }

//        for (int i = 0; i < 1000; i++) {
//            ByteBuf buffer = allocator.buffer(20 * 1024 * 1024);
//            buffer.release();
//            Logger.tcfo(buffer);
//            System.out.println("分配了 " + 20 * (i + 1) + " MB");
//        }

    }

    //Xmx1000m -Xms1000m
    @Test
    public void showPooledByteBufAllocator() {
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

        for (int i = 0; i < 1000; i++) {
            ByteBuf buffer = allocator.directBuffer(20 * 1024 * 1024);
//            buffer.release();
            Logger.tcfo(buffer);
            System.out.println("分配了 " + 20 * (i + 1) + " MB");
        }
    }

    @Test
    public void showAlloc() {


        ByteBuf buffer = null;
        //方法一：默认分配器，分配初始容量为9，最大容量100的缓冲
        buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);

        Logger.tcfo(buffer);
        //方法二：默认分配器，分配初始为256，最大容量Integer.MAX_VALUE 的缓冲
        buffer = ByteBufAllocator.DEFAULT.buffer();
        Logger.tcfo(buffer);

        //方法三：非池化分配器，分配基于Java的堆内存缓冲区
        buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        Logger.tcfo(buffer);

        //方法四：池化分配器，分配基于操作系统的管理的直接内存缓冲区
        buffer = PooledByteBufAllocator.DEFAULT.directBuffer();
        Logger.tcfo(buffer);
        //…..其他方法


    }

    @Test
    public void showAllocParam() {

        // -Dio.netty.allocator.type=unpooled   -Dio.netty.allocator.type=pooled
        // -Dio.netty.noPreferDirect=true      -Dio.netty.noPreferDirect=false     DIRECT_BUFFER_PREFERRED
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInboundHandlerAdapter() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBufAllocator allocator = ctx.alloc();

                Logger.tcfo(allocator);

                ctx.channel().alloc();
                ByteBuf buffer = allocator.buffer();
                Logger.tcfo(buffer);
//                ctx.fireChannelRead(msg);
            }
        });
        channel.writeInbound(new Object());


        ThreadUtil.sleepForEver();
    }
}