package com.crazymakercircle.iodemo.socketDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;


/**
 * 文件传输Client端
 */
public class BlockSendClient extends Socket
{

    private Socket client;

    private FileInputStream fis;

    private DataOutputStream outputStream;

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     *
     * @throws Exception
     */
    public BlockSendClient() throws Exception
    {
        super(NioDemoConfig.SOCKET_SERVER_IP
                , NioDemoConfig.SOCKET_SERVER_PORT);
        this.client = this;
     }

    /**
     * 向服务端传输文件
     *
     * @throws Exception
     */
    public void sendFile() throws Exception
    {
        try
        {
            String sourcePath = NioDemoConfig.SOCKET_SEND_FILE;
            String srcPath = IOUtil.getResourcePath(sourcePath);
            Logger.info("srcPath=" , srcPath);

            File file = new File(srcPath);
            if (file.exists())
            {
                fis = new FileInputStream(file);
                outputStream = new DataOutputStream(client.getOutputStream());

                // 长度

                outputStream.writeLong(file.length());
                outputStream.flush();
                // 文件名
                outputStream.writeUTF("copy_" + file.getName());
                outputStream.flush();


                // 开始传输文件
                Logger.debug("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1)
                {
                    outputStream.write(bytes, 0, length);
                    outputStream.flush();
                    progress += length;
                    Logger.debug("| " + (100 * progress / file.length()) + "% |");
                }
                Logger.debug("======== 文件传输成功 ========");
            }else {

                Logger.info("======== 文件传输失败, 文件不存在 ========");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {

            IOUtil.closeQuietly(fis);
            IOUtil.closeQuietly(outputStream);
            IOUtil.closeQuietly(client);

        }
    }

    /**
     * 入口
     *
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            BlockSendClient client = new BlockSendClient(); // 启动客户端连接
            client.sendFile(); // 传输文件
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
