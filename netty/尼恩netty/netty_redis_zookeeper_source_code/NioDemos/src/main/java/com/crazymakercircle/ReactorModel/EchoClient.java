package com.crazymakercircle.ReactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Dateutil;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class EchoClient {

    public void start() throws IOException {

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);

        // 1、获取通道（channel）
        SocketChannel socketChannel = SocketChannel.open(address);
        Logger.info("客户端连接成功");
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {

        }
        Logger.tcfo("客户端启动成功！");

        //启动接受线程
        Processor processor = new Processor(socketChannel);
        Commander commander = new Commander(processor);
        new Thread(commander).start();
        new Thread(processor).start();

    }

    static class Commander implements Runnable {
        Processor processor;

        Commander(Processor processor) throws IOException {
            //Reactor初始化
            this.processor = processor;
        }

        public void run() {
            while (!Thread.interrupted()) {

                ByteBuffer buffer = processor.getSendBuffer();

                Scanner scanner = new Scanner(System.in);
                while (processor.hasData.get()) {
                    Logger.tcfo("还有消息没有发送完，请稍等");
                    ThreadUtil.sleepMilliSeconds(1000);

                }
                Logger.tcfo("请输入发送内容:");
                if (scanner.hasNext()) {

                    String next = scanner.next();
                    buffer.put((Dateutil.getNow() + " >>" + next).getBytes());

                    processor.hasData.set(true);
                }

            }
        }
    }


    @Data
    static class Processor implements Runnable {
        ByteBuffer sendBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

        ByteBuffer readBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

        protected AtomicBoolean hasData = new AtomicBoolean(false);

        final Selector selector;
        final SocketChannel channel;

        Processor(SocketChannel channel) throws IOException {
            //Reactor初始化
            selector = Selector.open();

            this.channel = channel;
            channel.register(selector,
                    SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    Iterator<SelectionKey> it = selected.iterator();
                    while (it.hasNext()) {
                        SelectionKey sk = it.next();
                        if (sk.isWritable()) {

                            if (hasData.get()) {
                                SocketChannel socketChannel = (SocketChannel) sk.channel();
                                sendBuffer.flip();
                                // 操作三：发送数据
                                socketChannel.write(sendBuffer);
                                sendBuffer.clear();
                                hasData.set(false);
                            }

                        }
                        if (sk.isReadable()) {
                            // 若选择键的IO事件是“可读”事件,读取数据
                            SocketChannel socketChannel = (SocketChannel) sk.channel();

                            int length = 0;
                            while ((length = socketChannel.read(readBuffer)) > 0) {
                                readBuffer.flip();
                                Logger.info("server echo:" + new String(readBuffer.array(), 0, length));
                                readBuffer.clear();
                            }

                        }
                        //处理结束了, 这里不能关闭select key，需要重复使用
                        //selectionKey.cancel();
                    }
                    selected.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient().start();
    }
}
