package com.crazymakercircle.iodemo.socketDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 文件传输Server端
 * Created by 尼恩@ 疯创客圈
 */
public class NioReceiveServer {

    //接受文件路径
    private static final String RECEIVE_PATH = NioDemoConfig.SOCKET_RECEIVE_PATH;

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    static class Session {
        int step = 1; //1 读取文件名称的长度，2 读取文件名称  ，3 ，读取文件内容的长度， 4 读取文件内容
        //文件名称
        String fileName = null;

        //长度
        long fileLength;
        int fileNameLength;

        //开始传输的时间
        long startTime;

        //客户端的地址
        InetSocketAddress remoteAddress;

        //输出的文件通道
        FileChannel fileChannel;

        //接收长度
        long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }

    private ByteBuffer buffer
            = ByteBuffer.allocate(NioDemoConfig.SERVER_BUFFER_SIZE);

    //使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    Map<SelectableChannel, Session> clientMap = new HashMap<SelectableChannel, Session>();


    public void startServer() throws IOException {
        // 1、获取Selector选择器
        Selector selector = Selector.open();

        // 2、获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();

        // 3.设置为非阻塞
        serverChannel.configureBlocking(false);
        // 4、绑定连接
        InetSocketAddress address
                = new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.bind(address);
        // 5、将通道注册到选择器上,并注册的IO事件为：“接收新连接”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        Logger.tcfo("serverChannel is linstening...");
        // 6、轮询感兴趣的I/O就绪事件（选择键集合）
        while (selector.select() > 0) {
            if (null == selector.selectedKeys()) continue;
            // 7、获取选择键集合
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                // 8、获取单个的选择键，并处理
                SelectionKey key = it.next();
                if (null == key) continue;

                // 9、判断key是具体的什么事件，是否为新连接事件
                if (key.isAcceptable()) {
                    // 10、若接受的事件是“新连接”事件,就获取客户端新连接
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    if (socketChannel == null) continue;
                    // 11、客户端新连接，切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);

                    // 12、将客户端新连接通道注册到selector选择器上
                    SelectionKey selectionKey =
                            socketChannel.register(selector, SelectionKey.OP_READ);
                    // 余下为业务处理
                    Session session = new Session();
                    session.remoteAddress
                            = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, session);
                    Logger.debug(socketChannel.getRemoteAddress() + "连接成功...");

                } else if (key.isReadable()) {
                    handleData(key);
                }
                // NIO的特点只会累加，已选择的键的集合不会删除
                // 如果不删除，下一次又会被select函数选中
                it.remove();
            }
        }
    }

    /**
     * 处理客户端传输过来的数据
     */
    private void handleData(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        Session session = clientMap.get(key.channel());

        buffer.clear();
        while ((num = socketChannel.read(buffer)) > 0) {

           Logger.cfo("收到的字节数 = " + num);

                  //切换到读模式
            buffer.flip();

            process(session, buffer);
            buffer.clear();
//                key.cancel();

        }


    }

    // 非常抱歉，《netty zookeeper redis 高并发核心编程》 是08年08月出版的，3年了，当时候懒，没有解决文件传输的半包问题
    // 传输文件的时候，经常出问题
    // 很多小伙伴问我，怎么解决，我的答案都是： 半包问题，netty已经解决， nio的半包问题，不管也罢
    // 一直到今天，2021.12.3
    // 为了讲清楚 rocketmq， kakfa的 零复制， 才不得不来解决这个  nio 的半包问题
    // 但是，比较复杂的，需要对nio 有比较熟练的使用 和深入的理解
    // 下面的方案，只能说解决了80%
    // 并没有完全解决， 但是，已经基本传输文件没有问题了
    // 具体的解决过程，和解决的技巧，咱们 视频见
    // 总之， nio 比较复杂，但是非常重要， 大家一定要从骨子里掌握

    private void process(Session session, ByteBuffer buffer) {
        while (len(buffer) > 0) {   //客户端发送过来的，首先处理文件名长度
            if (1 == session.step) {
                int fileNameLengthByteLen = len(buffer);
                System.out.println("读取文件名称长度之前，可读取的字节数 = " + fileNameLengthByteLen);
                System.out.println("读取文件名称长度之前，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件名称长度之前，buffer.capacity() = " + buffer.capacity());
                System.out.println("读取文件名称长度之前，buffer.limit() = " + buffer.limit());
                System.out.println("读取文件名称长度之前，buffer.position() = " + buffer.position());

                if (len(buffer) < 4) {
                    Logger.cfo("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }

                //获取文件名称长度
                session.fileNameLength = buffer.getInt();

                System.out.println("读取文件名称长度之后，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件名称长度 = " + session.fileNameLength);

                session.step = 2;

            } else if (2 == session.step) {
                Logger.cfo("step 2");

                if (len(buffer) < session.fileNameLength) {
                    Logger.cfo("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }

                byte[] fileNameBytes = new byte[session.fileNameLength];


                //读取文件名称
                buffer.get(fileNameBytes);


                // 文件名
                String fileName = new String(fileNameBytes, charset);
                System.out.println("读取文件名称 = " + fileName);

                File directory = new File(RECEIVE_PATH);
                if (!directory.exists()) {
                    directory.mkdir();
                }
                Logger.info("NIO  传输目标dir：", directory);

                session.fileName = fileName;
                String fullName = directory.getAbsolutePath() + File.separatorChar + fileName;
                Logger.info("NIO  传输目标文件：", fullName);

                File file = new File(fullName.trim());

                try {
                    if (!file.exists()) {
                        file.createNewFile();

                    }
                    FileChannel fileChannel = new FileOutputStream(file).getChannel();
                    session.fileChannel = fileChannel;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                session.step = 3;

            } else if (3 == session.step) {
                Logger.cfo("step 3");

                //客户端发送过来的，首先处理文件内容长度

                if (len(buffer) < 4) {
                    Logger.cfo("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }
                //获取文件内容长度
                session.fileLength = buffer.getInt();

                System.out.println("读取文件内容长度之后，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件内容长度 = " + session.fileLength);

                session.step = 4;
                session.startTime = System.currentTimeMillis();

            } else if (4 == session.step) {

                Logger.cfo("step 4");
                //客户端发送过来的，最后是文件内容

                session.receiveLength += len(buffer);

                // 写入文件
                try {
                    session.fileChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (session.isFinished()) {
                    finished(session);
                }
            }
        }
    }


    private void finished(Session session) {
        IOUtil.closeQuietly(session.fileChannel);
        Logger.info("上传完毕");
        Logger.debug("文件接收成功,File Name：" + session.fileName);
        Logger.debug(" Size：" + IOUtil.getFormatFileSize(session.fileLength));
        long endTime = System.currentTimeMillis();
        Logger.debug("NIO IO 传输毫秒数：" + (endTime - session.startTime));
    }


    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        NioReceiveServer server = new NioReceiveServer();
        server.startServer();
    }

    private static int len(ByteBuffer buffer) {

        Logger.cfo(" >>>  buffer left：" + buffer.remaining());
        return buffer.remaining();
    }

}