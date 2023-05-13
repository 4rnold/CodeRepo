package com.crazymakercircle.netty.encoder;

import com.crazymakercircle.util.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class String2IntegerEncoder extends MessageToMessageEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext c, String s, List<Object> list) throws Exception {

        Logger.info(s);

        char[] array = s.toCharArray();

        for (char a : array) {

            //48 是0的编码，57 是9 的编码
            if (a >= 48 && a <= 57) {

                list.add(new Integer(a));

            }
        }
    }
}
