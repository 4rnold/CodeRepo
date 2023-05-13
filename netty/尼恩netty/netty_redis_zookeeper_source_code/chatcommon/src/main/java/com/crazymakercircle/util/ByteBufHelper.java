package com.crazymakercircle.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufHelper {

    /**
     * 转为字符串
     *
     * @param buf
     * @return
     */
    static public String byteBuf2String(ByteBuf buf) {
        String str;
        if (buf.hasArray()) {
            // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else {
            // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            try {
                str = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return str;
    }

    /**
     * String 转换 ByteBuffer
     *
     * @param str
     * @return
     */
    public static ByteBuffer getByteBuffer(String str) {
        return ByteBuffer.wrap(str.getBytes());
    }


    /**
     * String 转换 ByteBuffer
     *
     * @param str
     * @return
     */
    public static ByteBuf byteBufferWithLengthHead(String str) {
        byte[] bytes = str.getBytes();

        ByteBuf bodyBuf = Unpooled.wrappedBuffer(bytes);
        ByteBuf headerBuf = Unpooled.buffer(4).order(ByteOrder.BIG_ENDIAN);
        headerBuf.writeInt(bytes.length);
        ByteBuf allByteBuf = Unpooled.wrappedBuffer(headerBuf, bodyBuf);
        return allByteBuf;
    }


}
