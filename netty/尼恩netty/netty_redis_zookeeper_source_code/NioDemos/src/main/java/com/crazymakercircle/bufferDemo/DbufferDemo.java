package com.crazymakercircle.bufferDemo;

import com.crazymakercircle.util.Logger;
import org.junit.Test;

import java.nio.ByteBuffer;

public class DbufferDemo {

    @Test
    public void testDirectByteBuffer() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ByteBuffer intBuffer = ByteBuffer.allocateDirect(1024);
        Logger.debug("------------after allocate------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());

        for (int i = 0; i < 5; i++) {
            intBuffer.putInt(i);

        }

        Logger.debug("------------after putTest------------------");
        Logger.debug("position=" + intBuffer.position());
        Logger.debug("limit=" + intBuffer.limit());
        Logger.debug("capacity=" + intBuffer.capacity());
        intBuffer.flip();
        for (int i = 0; i < 5; i++) {
            int j = intBuffer.getInt();
            Logger.debug("j = " + j);
        }

  }
}
