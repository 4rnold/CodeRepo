package com.crazymakercircle.iodemo.socketDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;

import static com.crazymakercircle.util.ByteUtil.utf8;

/**
 * 文件传输Client端
 * Created by 尼恩@ 疯创客圈
 */

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class NettyFastSendClient {

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    public NettyFastSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public class FileSendHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            String srcPath = (String) msg;
            byte[] fileNameBytes = utf8(srcPath);


            File file = new File(srcPath);
            if (!file.exists()) {
                srcPath = IOUtil.getResourcePath(srcPath);
                file = new File(srcPath);
                Logger.debug("srcPath=" + srcPath);
                if (!file.exists()) {
                    Logger.debug("文件不存在");
                    return;
                }

            }

            RandomAccessFile raf = null;
            long length = -1;
            try {
                raf = new RandomAccessFile(file, "r");
                length = raf.length();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.info("发送异常");
                ctx.channel().close();
                return;
            } finally {
                if (length < 0 && raf != null) {
                    raf.close();
                    ctx.channel().close();
                }
            }

            ByteBuf outBuf = ctx.alloc().buffer();

            outBuf.writeInt(fileNameBytes.length);

            outBuf.writeBytes(fileNameBytes);

            outBuf.writeInt((int) length);

            ctx.writeAndFlush(outBuf);

            Logger.info("文件长度：" + length);
            ChannelFuture future = null;

            if (ctx.pipeline().get(SslHandler.class) == null) {

                DefaultFileRegion fileRegion = new DefaultFileRegion(raf.getChannel(), 0, length);
                // 传输文件使用了 DefaultFileRegion 进行写入到 NioSocketChannel 中
                future = ctx.write(fileRegion);

            } else {
                // SSL enabled - cannot use zero-copy file transfer.
                future = ctx.write(new ChunkedFile(raf));
            }
            future.addListener(f -> {

                if (f.isSuccess()) {
                    Logger.info("发送完成");
                    ctx.channel().close();
                }

            });

        }
    }


    public void runClient() {
        //创建reactor 线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                //有连接到达时会创建一个channel
                protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ch.pipeline().addLast(new FileSendHandler());
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) ->
            {
                if (futureListener.isSuccess()) {
                    Logger.info("客户端连接成功!");

                } else {
                    Logger.info("客户端连接失败!");
                }

            });

            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();
//            String srcPath = NioDemoConfig.SOCKET_SEND_BIG_FILE;
            String srcPath = NioDemoConfig.SOCKET_SEND_FILE;
            channel.writeAndFlush(srcPath);


            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = NioDemoConfig.SOCKET_SERVER_PORT;
        String ip = NioDemoConfig.SOCKET_SERVER_IP;
        new NettyFastSendClient(ip, port).runClient();
    }
}