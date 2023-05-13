package com.crazymakercircle.iodemo.zeroCopy;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static com.crazymakercircle.config.Constants.BIG_BUFFER_SIZE;


/**
 * 文件传输Client端
 * Created by 尼恩@ 疯创客圈
 */

public class BigFileNioFastSendClient {


    /**
     * 构造函数
     * 与服务器建立连接
     *
     * @throws Exception
     */
    public BigFileNioFastSendClient() {

    }

    private Charset charset = Charset.forName("UTF-8");

    /**
     * 向服务端传输文件
     *
     * @throws Exception
     */
    public void sendFile() {
        try {


            //发送小文件
            String srcPath = NioDemoConfig.SOCKET_SEND_FILE;
            //发送一个大的
//            String srcPath = NioDemoConfig.SOCKET_SEND_BIG_FILE;
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


            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            socketChannel.socket().connect(
                    new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP
                            , NioDemoConfig.SOCKET_SERVER_PORT));
            socketChannel.configureBlocking(false);

            Logger.debug("Client 成功连接服务端");

            while (!socketChannel.finishConnect()) {
                //不断的自旋、等待，或者做一些其他的事情
            }

            //发送文件名称
            ByteBuffer fileNameByteBuffer = charset.encode(file.getName());

            ByteBuffer buffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
 //            int fileNameLen =     fileNameByteBuffer.capacity();
             int fileNameLen = fileNameByteBuffer.remaining();
            buffer.clear();
            buffer.putInt(fileNameLen);
            //切换到读模式
            buffer.flip();
            socketChannel.write(buffer);
            Logger.info("Client 文件名称长度发送完成:", fileNameLen);

            //清空
            buffer.clear();

            // 发送文件名称
            socketChannel.write(fileNameByteBuffer);
            Logger.info("Client 文件名称发送完成:", file.getName());
            //发送文件长度
            buffer.putInt((int) file.length());
            //切换到读模式
            buffer.flip();
            //写入文件长度
            socketChannel.write(buffer);
            buffer.clear();
            Logger.info("Client 文件长度发送完成:", file.length());

            //使用sendfile:读取磁盘文件，并网络发送
            FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();

            //发送文件内容
            Logger.debug("使用sendfile，开始传输文件：" + fileChannel.size());


            // 零拷贝传输数据, 注意记录每次拷贝的起始位置
            long transferLen;
            long totalCount = 0;
            // 使用零拷贝将文件数据传到服务器, 循环终止条件是传输结果小于等于 0
            while ( totalCount< file.length()) {
                 //kafka 使用了这个 transferTo
                //一次传输128M
                transferLen = fileChannel.transferTo(totalCount, BIG_BUFFER_SIZE, socketChannel);
                Logger.debug(" 此次 文件传输完成:"+transferLen);
                totalCount += transferLen;
            }

            Logger.debug("======== 文件传输成功 ========");


            //等待一分钟关闭连接
            ThreadUtil.sleepSeconds(60);
            // 关闭连接
            socketChannel.close();
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args) {

        BigFileNioFastSendClient client = new BigFileNioFastSendClient(); // 启动客户端连接
        client.sendFile(); // 传输文件


    }

}
