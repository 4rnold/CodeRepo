package com.crazymakercircle.netty.protocol;

import com.crazymakercircle.util.ByteBufHelper;
import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.crazymakercircle.util.ByteBufHelper.byteBufferWithLengthHead;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class JsonMsgDemo {

    //构建Json对象
    public JsonMsg buildMsg() {
        JsonMsg user = new JsonMsg();
        user.setId(1000);
        user.setContent("疯狂创客圈:高性能学习社群");
        return user;
    }

    //序列化 serialization & 反序列化 Deserialization
    @Test
    public void serAndDesr() throws IOException {

        JsonMsg message = buildMsg();

        //将POJO对象，序列化成字符串
        String json = message.convertToJson();

        //可以用于网络传输,保存到内存或外存
        Logger.info("json:=" + json);

        //JSON 字符串,反序列化成对象POJO
        JsonMsg inMsg = JsonMsg.parseFromJson(json);

        Logger.info("devId:=" + inMsg.getId());
        Logger.info("content:=" + inMsg.getContent());
    }

    /**
     * JsonMsg 2 StringEncoder
     */
    public class JsonMsg2StringEncoder extends MessageToMessageEncoder<JsonMsg> {
        @Override
        protected void encode(ChannelHandlerContext c,
                              JsonMsg jsonMsg,
                              List<Object> list) throws Exception {
            list.add(jsonMsg.convertToJson());
        }
    }

    /**
     * 编码实例
     */
    @Test
    public void testJsonEncoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                // 客户端channel流水线添加2个handler处理器
                ch.pipeline().addLast(new LengthFieldPrepender(4));
                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                ch.pipeline().addLast(new JsonMsg2StringEncoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0; j < 100; j++) {

            channel.writeOutbound(new JsonMsg(j));
        }
        channel.flush();

        ByteBuf outmsg = channel.readOutbound();
        while (null!=outmsg &&  outmsg.readableBytes() > 0) {
            Logger.info(ByteBufHelper.byteBuf2String(outmsg));
            outmsg = channel.readOutbound();
        }

    }

    /**
     * 解码实例
     */
    @Test
    public void testJsonDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                // pipeline管理子通道channel中的Handler
                // 向子channel流水线添加3个handler处理器
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                        1024, 0,
                        4, 0, 4));
                ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                ch.pipeline().addLast(new JsonMsgDecoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0; j < 100; j++) {
            String json = new JsonMsg(j).convertToJson();
            ByteBuf inBoundBytes = byteBufferWithLengthHead(json);
            channel.writeInbound(inBoundBytes);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
