package com.crazymakercircle.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Iterator;

import static com.crazymakercircle.netty.bytebuf.PrintAttribute.print;
import static com.crazymakercircle.util.ByteUtil.utf8;
import static com.crazymakercircle.util.ByteUtil.utf8Code;


public class CompositeBufferTest {

    @Test
    public void intCompositeBufComposite() {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer(3);
        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{1, 2, 3}));
        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        compositeByteBuf.addComponent(Unpooled.wrappedBuffer(new byte[]{5, 6}));



        print("动作：addComponent ", compositeByteBuf);
        showMsg(compositeByteBuf);

        iterateMsg(compositeByteBuf);

        //合并成一个单独的缓冲区
        ByteBuffer nioBuffer = compositeByteBuf.nioBuffer(0, 6);


        byte[] bytes = nioBuffer.array();
        System.out.print("bytes = ");
        for (byte b : bytes) {
            System.out.print(b);
        }
        compositeByteBuf.release();
    }

    @Test
    public void byteBufComposite() {
        //消息头
        ByteBuf headerBuf = Unpooled.wrappedBuffer(utf8("疯狂创客圈:"));
        //消息体1
        ByteBuf bodyBuf = Unpooled.wrappedBuffer(utf8("高性能 Netty"));


        //深层次复制

        ByteBuf dstBuf= ByteBufAllocator.DEFAULT.buffer();
        dstBuf.writeBytes(headerBuf.slice());
        dstBuf.writeBytes(bodyBuf.slice());
        print("动作：dstBuf ", dstBuf);
        showMsg(dstBuf);

        //零复制
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeByteBuf.addComponents(headerBuf.slice(), bodyBuf.slice());
        print("动作：addComponent 1", compositeByteBuf);
        showMsg(compositeByteBuf);

        iterateMsg(compositeByteBuf);

        headerBuf.retain();
        compositeByteBuf.release();

        compositeByteBuf = Unpooled.compositeBuffer(2);

        //消息体2
        bodyBuf = Unpooled.wrappedBuffer(utf8("高性能学习社群, 卷王 社群"));
        compositeByteBuf.addComponents(headerBuf.slice(), bodyBuf.slice());


        print("动作：addComponent 2", compositeByteBuf);

        showMsg(compositeByteBuf);

        iterateMsg(compositeByteBuf);

        compositeByteBuf.release();
    }



    private void showMsg(ByteBuf b) {
        System.out.println(" showMsg ..........");
                //处理整个消息
        int length = b.readableBytes();
        byte[] array = new byte[length];
        //将CompositeByteBuf中的数据复制到数组中
        b.getBytes(b.readerIndex(), array);
        //处理一下数组中的数据
        System.out.println(" content： " + new String(array, utf8Code));


    }

    private void iterateMsg(CompositeByteBuf cbuf) {
        System.out.println(" iterateMsg .......... ");

        Iterator<ByteBuf> it = cbuf.iterator();
        while (it.hasNext()) {
            ByteBuf b = it.next();
            int length = b.readableBytes();
            byte[] array = new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            b.getBytes(b.readerIndex(), array);
            //处理一下数组中的数据
            System.out.print(new String(array, utf8Code));
        }

        System.out.println();

        //处理整个消息
        for (ByteBuf b : cbuf) { // for in
            int length = b.readableBytes();
            byte[] array = new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            b.getBytes(b.readerIndex(), array);
            //处理一下数组中的数据
            System.out.print(new String(array, utf8Code));
        }
        System.out.println();
    }


    @Test
    public void byteBufWrapper() {
        //消息头
        ByteBuf headerBuf = Unpooled.wrappedBuffer(utf8("疯狂创客圈:"));
        //消息体1
        ByteBuf bodyBuf = Unpooled.wrappedBuffer(utf8("高性能 Netty"));


       print("动作：headerBuf ", headerBuf);
        showMsg(headerBuf);

        //零复制
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(headerBuf.slice(), bodyBuf.slice());
        print("动作：addComponent 1", wrappedBuffer);


        showMsg(wrappedBuffer);
        iterateMsg((CompositeByteBuf) wrappedBuffer);


        headerBuf.retain();
        wrappedBuffer.release();


        //消息体2
        bodyBuf = Unpooled.wrappedBuffer(utf8("高性能学习社群, 卷王 社群"));
        wrappedBuffer = Unpooled.wrappedBuffer(headerBuf.slice(), bodyBuf.slice());


        print("动作：addComponent 2", wrappedBuffer);

        showMsg(wrappedBuffer);

        iterateMsg((CompositeByteBuf) wrappedBuffer);
        wrappedBuffer.release();
    }
}