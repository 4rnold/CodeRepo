package com.crazymakercircle.ReactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//反应器
class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        //Reactor初始化
        selector = Selector.open();

        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);

        //非阻塞
        serverSocket.configureBlocking(false);


        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selector,0,new AcceptorHandler());

        // SelectionKey.OP_ACCEPT
        serverSocket.socket().bind(address);
        Logger.info("服务端已经开始监听："+address);


        sk.interestOps(SelectionKey.OP_ACCEPT);

        //attach callback object, AcceptorHandler
        //sk.attach(new AcceptorHandler());
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {

                //io事件的查询
                // 限时阻塞查询
                selector.select(1000);


                Set<SelectionKey> selected = selector.selectedKeys();
                if (null == selected || selected.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    SelectionKey sk = it.next();

                    it.remove();     //避免下次重复处理

                    dispatch(sk);
                }
//                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                Logger.info("接收到一个连接");
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
