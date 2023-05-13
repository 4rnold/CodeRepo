package com.crazymakercircle.iodemo.fileDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.JvmUtil;
import com.crazymakercircle.util.LibC;
import com.crazymakercircle.util.Logger;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.junit.Test;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static com.crazymakercircle.util.FileUtil.unmap;

/**
 * Created by 尼恩@ 疯创客圈
 */
public class FileMmapDemo {

    public static final int CAPACITY = 1024;

    public static String demoPath = NioDemoConfig.MMAP_DEMO_PATH_FILE;
    public static String lockMemDemoPath = NioDemoConfig.LOCK_MEM_DEMO_PATH_FILE;
    private byte[] bytes;


    /**
     * mmap读写文件
     */
    @Test
    public  void mmapWriteFile() {

        String sourcePath = NioDemoConfig.MMAP_FILE_RESOURCE_SRC_PATH;
        String decodePath = IOUtil.getResourcePath(sourcePath);

        Logger.debug("decodePath=" + decodePath);

        //向文件中存1k的数据
        int length = 1024;//
        try (FileChannel channel = new RandomAccessFile(decodePath, "rw").getChannel();) {

            //一个整数4个字节
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);

            // 内存的写入
            for (int i = 0; i < length; i++) {
                mapBuffer.put((byte) (Integer.valueOf('a') + i % 26));
            }

            // 内存的读取
            for (int i = 0; i < length; i++) {
                if (i % 50 == 0) System.out.println("");
                //像数组一样访问
                System.out.print((char) mapBuffer.get(i));
            }

            mapBuffer.force();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取文件内容并输出
     */

    @Test
    public  void mmapPrivate() {

        String sourcePath = NioDemoConfig.MMAP_FILE_RESOURCE_SRC_PATH;
        String decodePath = IOUtil.getResourcePath(sourcePath);

        Logger.debug("decodePath=" + decodePath);

        //向文件中存1M的数据
        int length = 1024;
        //通过RandomAccessFile获取FileChannel。
        try (FileChannel channel = new RandomAccessFile(decodePath, "rw").getChannel();) {

            //通过channel进行内存映射，获取一个虚拟内存区域VMA
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.PRIVATE, 0, length);
            for (int i = 0; i < length; i++) {
//                mapBuffer.put((byte) (Integer.valueOf('a') + i % 26));
                mapBuffer.put((byte) (Integer.valueOf('a') +0));
            }
            for (int i = 0; i < length; i++) {
                if (i % 50 == 0) System.out.println("");
                //像数组一样访问
                System.out.print((char) mapBuffer.get(i));
            }

            mapBuffer.force();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void mmapDemo() {
        File file1 = new File(demoPath);

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file1, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;

        }

        int len = 2048;
        // 映射为2kb，那么生成的文件也是2kb
        MappedByteBuffer mappedByteBuffer = null;
        try {
            mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Logger.cfo(mappedByteBuffer.isReadOnly());


        Logger.cfo(mappedByteBuffer.position());
        Logger.cfo(mappedByteBuffer.limit());

        // 写数据之后，JVM 退出之后会强制刷新的

        try {
            bytes = ("疯狂创客圈 Java 高并发 卷王社群  ").getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return;
        }
        //mappedByteBuffer.write(40, bytes.length, bytes);
        mappedByteBuffer.put(bytes);


        Logger.cfo(mappedByteBuffer.position());
        Logger.cfo(mappedByteBuffer.limit());

        mappedByteBuffer.force();

        // 参考OffsetIndex强制回收已经分配的mmap，不必等到下次GC，
        try {
            unmap(mappedByteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 在Windows上需要执行unmap(mappedByteBuffer); 否则报错
        // Windows won't let us modify the file length while the file is mmapped
        // java.io.IOException: 请求的操作无法在使用用户映射区域打开的文件上执行
        try {
            randomAccessFile.setLength(len / 2);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, len / 2);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        // A mapping, once established, is not dependent upon the file channel
        // that was used to create it.
        // Closing the channel, in particular, has no effect upon the validity of the mapping.
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mappedByteBuffer.position(bytes.length);
        mappedByteBuffer.put(bytes);

        try {
            mappedByteBuffer.position(98);
            bytes = ("高并发发烧友 圈子 ").getBytes("UTF-8");
            mappedByteBuffer.put(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mappedByteBuffer.force();

    }

    public static final int OS_PAGE_SIZE = 1024 * 4;


    public static void main(String[] args) throws Exception {
        final long beginTime = System.currentTimeMillis();


        //文件名称
        String fileName = lockMemDemoPath;

        //文件预热
        boolean warmMappedFile = true;

        if (args.length > 1 && args[1] != null) {
            fileName = args[1];
        }

        Logger.cfo("fileName= " + fileName);

        if (args.length > 2 && args[2] != null) {
            warmMappedFile = Boolean.parseBoolean(args[2]);
        }

        Logger.cfo("warmMappedFile= " + warmMappedFile);

        int fileSize = 1024 * 1024 * 1024;

        File file = new File(fileName);
        FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
        MappedByteBuffer mappedByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);


        if (warmMappedFile) {
            for (int i = 0, j = 0; i < fileSize; i += OS_PAGE_SIZE, j++) {
                mappedByteBuffer.put(i, (byte) 0);

                if (i > 100 && i < 200) {
                    mappedByteBuffer.force();
                }
            }


            final long address = ((DirectBuffer) (mappedByteBuffer)).address();
            Pointer pointer = new Pointer(address);
            {
                int ret = LibC.INSTANCE.mlock(pointer, new NativeLong(fileSize));
                Logger.cfo("mlock {} {} {} ret = {} time consuming = {}", address, fileName, fileSize, ret, System.currentTimeMillis() - beginTime);

            }
            {
                int ret = LibC.INSTANCE.madvise(pointer, new NativeLong(1024 * 1024 * 102), LibC.MADV_WILLNEED);
                Logger.cfo("madvise {} {} {} ret = {} time consuming = {}", address, fileName, fileSize, ret, System.currentTimeMillis() - beginTime);

            }

        }
        Logger.cfo("锁定内存后，等待5秒打印 进程的 内存映射");


        //使用cat /proc/进程id/maps 查看进程内存映射
        FormattingTuple format = MessageFormatter.arrayFormat("cat /proc/{}/maps", new Object[]{JvmUtil.getProcessID()});

        System.out.println(format.getMessage());

        Thread.sleep(1000000000);


    }
}
