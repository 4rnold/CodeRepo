package com.crazymakercircle.iodemo.sharemem;

import com.crazymakercircle.util.ThreadUtil;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;


/**
 * 共享内存操作类
 */
public class ShareRead {


    public static void main(String arsg[]) throws Exception {
        ShareMemory sm = new ShareMemory();
        String model = "卷王社群-卷王社群-1000000";
        int length = model.getBytes("UTF-8").length;
        ThreadUtil.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                try {
                    byte[] b = new byte[length];
                    sm.read(40, length, b);
                    System.out.println("finish read:" + new String(b, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        }, 1, TimeUnit.SECONDS);


    }
}  