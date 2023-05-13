package com.crazymakercircle.iodemo.sharemem;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Properties;


/**
 * 共享内存操作类
 */
public class ShareMemory {
    String sourcePath = NioDemoConfig.MEM_SHARE_RESOURCE_SRC_PATH;
    String decodePath = IOUtil.getResourcePath(sourcePath);

    int fsize = 1024;                          //文件的实际大小  
    MappedByteBuffer mapBuf = null;         //定义共享内存缓冲区
    FileChannel fc = null;                  //定义相应的文件通道  
    FileLock fl = null;                     //定义文件区域锁定的标记。        
    Properties p = null;
    RandomAccessFile randomAccessFile = null;         //定义一个随机存取文件对象


    public ShareMemory() {


        try {
            // 获得一个只读的随机存取文件对象   "rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。    
            randomAccessFile = new RandomAccessFile(decodePath, "rw");
            //获取相应的文件通道  
            fc = randomAccessFile.getChannel();
             //将此通道的文件区域直接映射到虚拟空间中。
            mapBuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, fsize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pos  锁定区域开始的位置；必须为非负数
     * @param len  锁定区域的大小；必须为非负数
     * @param buff 写入的数据
     * @return
     */
    public synchronized int write(int pos, int len, byte[] buff) {
        if (pos >= fsize || pos + len >= fsize) {
            return 0;
        }
        //定义文件区域锁定的标记。  
        FileLock fl = null;
        try {
            //获取此通道的文件给定区域上的锁定。   
            fl = fc.lock(pos, len, false);
            if (fl != null) {

                mapBuf.position(pos);
                ByteBuffer bf1 = ByteBuffer.wrap(buff);
                mapBuf.put(bf1);
                //释放此锁定。  
                fl.release();

                return len;
            }
        } catch (Exception e) {
            if (fl != null) {
                try {
                    fl.release();
                } catch (IOException e1) {
                    System.out.println(e1.toString());
                }
            }
            return 0;
        }

        return 0;
    }

    /**
     * @param pos  锁定区域开始的位置；必须为非负数
     * @param len  锁定区域的大小；必须为非负数
     * @param buff 要取的数据
     * @return
     */
    public synchronized int read(int pos, int len, byte[] buff) {
        if (pos >= fsize) {
            return 0;
        }
        //定义文件区域锁定的标记。  
        FileLock fl = null;
        try {
            fl = fc.lock(pos, len, true);
            if (fl != null) {
                //System.out.println( "pos="+pos );  
                mapBuf.position(pos);
                if (mapBuf.remaining() < len) {
                    len = mapBuf.remaining();
                }

                if (len > 0) {
                    mapBuf.get(buff, 0, len);
                }

                fl.release();

                return len;
            }
        } catch (Exception e) {
            if (fl != null) {
                try {
                    fl.release();
                } catch (IOException e1) {
                    System.out.println(e1.toString());
                }
            }
            return 0;
        }

        return 0;
    }

    /**
     * 完成，关闭相关操作
     */
    protected void finalize() throws Throwable {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            fc = null;
        }

        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            randomAccessFile = null;
        }
        mapBuf = null;
    }

    /**
     * 关闭共享内存操作
     */
    public synchronized void closeSMFile() {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            fc = null;
        }

        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            randomAccessFile = null;
        }
        mapBuf = null;
    }


}  