package com.crazymakercircle.iodemo.OIO;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.crazymakercircle.util.ByteUtil.utf8;

public class Client7000MultThread {

    //netty4中的时间轮已经不用了,用delayqueue替换了

    static HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 16);

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 70000; i++) {

            Socket socket = new Socket("localhost", NioDemoConfig.SOCKET_SERVER_PORT);

            Logger.info("完成了客户端的创建：", i);
            Logger.info("连接的两个端口:", socket.getPort(), socket.getLocalPort());

            new Thread(new Handler(i, socket)).start();
        }
        ThreadUtil.sleepForEver();
    }

    static class Handler implements Runnable {

        final Socket socket;

        private final int index;

        private CountDownLatch latch;

        Handler(int i, Socket s) {
            socket = s;
            index = i;
            System.out.println("发送的字符串：from socket index" + socket.getLocalPort());

        }

        public void run() {
            while (true) {


                latch =new CountDownLatch(1);
                if (socket != null) {
                    System.out.println(index +" 客户端连接服务器成功！");
                }


                timer.newTimeout((Timeout timeout) -> {
                    try {
                        socket.getOutputStream().write(utf8(("from " + index)));
                        latch.countDown();
//                        socket.getOutputStream().write(utf8(("from " + socket.getLocalPort())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }, 600, TimeUnit.SECONDS);

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}