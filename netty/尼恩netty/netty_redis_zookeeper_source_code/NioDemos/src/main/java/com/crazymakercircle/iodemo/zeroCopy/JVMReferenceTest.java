package com.crazymakercircle.iodemo.zeroCopy;

import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import org.junit.Test;

import java.lang.ref.*;
import java.util.Arrays;
import java.util.List;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class JVMReferenceTest {
    @Test
    public void testWeakReference() {
        String s = new String("我是Java卷王");
        WeakReference<String> wrf = new WeakReference<String>(s);
        s = null;
        Logger.cfo(wrf.get());

        System.gc();
         Logger.cfo(wrf.get());

    }

    @Test
    public void testSoftReference() {
        String s = new String("我是Java卷王");
        SoftReference<String> wrf = new SoftReference<String>(s);
        s = null;
        Logger.cfo(wrf.get());


        System.gc();
        System.runFinalization();
        Logger.cfo(wrf.get());
    }

    @Test
    public void testPhantomReference() {
        String s = new String("我是Java卷王");
        ReferenceQueue rq = new ReferenceQueue();

        PhantomReference<String> wrf = new PhantomReference<String>(s, rq);
        Logger.cfo("wrf="+wrf);

        s = null;
        Logger.cfo(wrf.get());
        System.gc();
        while (true) {
            Reference r = rq.poll();
            if (r != null) {
              Logger.cfo("r="+r);
            }

            ThreadUtil.sleepMilliSeconds(1000);
        }
    }

    // 模拟clearner
    static ReferenceQueue<List<String>> queue = new ReferenceQueue<>();

    class Cleaner extends PhantomReference<List<String>> {
        Cleaner(List<String> referent) {
            super(referent, queue);

        }

        void doWhenClean() {
            Logger.cfo("do sth WhenClean :");
        }
    }

    @Test
    public void testCleaner() {

        List<String> list = Arrays.asList("foo", "bar");

        Cleaner phantom = new Cleaner(list);
        list = null;
        System.gc();

        Logger.cfo("phantom.get:=" + phantom.get());

        while (true) {
            Cleaner cleaner = (Cleaner) queue.poll();
            if (cleaner != null) {
                cleaner.doWhenClean();

            }

            ThreadUtil.sleepMilliSeconds(1000);
        }
    }


}
