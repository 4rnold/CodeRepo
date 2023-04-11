package com.itheima.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.itheima.domain.ConfigInfo;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

/**
 * 使用kryo序列化和反序列化java对象
 *
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class KryoSerializeUtil {

    private static Kryo kryo;

    static {
        kryo = new Kryo();
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        kryo.register(Properties.class);
        kryo.register(String[].class);
        kryo.register(ConfigInfo.class);
        kryo.register(Date.class);
        kryo.register(JavassistLazyInitializer.class);
        kryo.register(Method.class);
        kryo.register(byte[].class);
        kryo.register(Class.class);
        kryo.register(Class[].class);
        kryo.register(HibernateProxy.class);
        kryo.register(Void.class);
        kryo.register(Void.TYPE);
        kryo.register(Timestamp.class);
    }
    /**
     * 把java对象序列化成byte数组
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        //1.判断序列化的对象是否为null
        if(object==null) {
            return null;
        }
        //2.定义缓冲区输出流
        ByteArrayOutputStream baos = null;
        //3.定义Kryo的输出对象
        Output output = null;
        try {
            //4.实例化kryo核心对象
            kryo.register(object.getClass());
            //5.实例化字节数组输出流
            baos = new ByteArrayOutputStream();
            //6.使用字节数组输出流构建Kryo的输出对象
            output = new Output(baos,100000);
            //7.使用kryo核心对象把object写入到output中
            kryo.writeObject(output, object);
            //8.刷出缓冲区
            output.flush();
            //9.把缓冲区内容转成字节数并返回
            return baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(output != null){
                try {
                    output.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把byte数组反序列化得到java对象
     * @param bytes 字节数组
     * @param clazz 需要转成的java对象字节码
     * @return
     */
    public static <T> T unserialize(byte[] bytes, Class<T> clazz) {
        //1.验证需要转换的数据
        if(bytes==null || bytes.length==0) {
            return null;
        }
        //2.实例化Kryo核心对象
        kryo.register(clazz);
        //3.根据传入的字节数组创建输入对象
        Input input = new Input(bytes,0,bytes.length);
        //4.把字节数组根据字节码转成指定对象
        T obj = kryo.readObject(input, clazz);
        //5.释放资源
        input.close();
        //6.返回对象
        return obj;
    }
}
