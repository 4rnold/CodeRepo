package com.crazymakercircle.iodemo.sharemem;

import com.crazymakercircle.util.ThreadUtil;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;


/**
 * 共享内存操作类
 */
public class ShareWrite {


    public static void main(String arsg[]) throws Exception {
        ShareMemory sm = new ShareMemory();

        ThreadUtil.scheduleAtFixedRate(new Runnable() {
            int index = 0;

            @Override
            public void run() {
                index++;
                String str = "卷王社群-卷王社群-" + index;
                byte[] bytes = null;
                try {
                    bytes = str.getBytes("UTF-8");
                    sm.write(40, bytes.length, bytes);
                    System.out.println("finish write:" + new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        }, 1, TimeUnit.SECONDS);


    }
}  