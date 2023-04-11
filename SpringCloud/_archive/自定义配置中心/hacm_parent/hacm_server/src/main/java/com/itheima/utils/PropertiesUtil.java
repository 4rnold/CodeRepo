package com.itheima.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 把用户上传的文件转成properties文件
 *
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class PropertiesUtil {


    /**
     * 转换Properties文件
     * @param bytes 文件对象
     * @param suffix        文件类型
     * @return
     */
    public static Properties toProperties(byte[] bytes, String suffix) {
        //1.定义返回值
        Properties properties = null;
        try {
            //2.判断是properties还是yml
            if ("yml".equals(suffix)) {
//                Yaml yaml = new Yaml();
//                properties = yaml.loadAs(multipartFile.getInputStream(),Properties.class);

                //创建yaml文件解析工厂
                YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
                //设置资源内容
                yamlPropertiesFactoryBean.setResources(new ByteArrayResource(bytes));
                //获取properties对象
                properties = yamlPropertiesFactoryBean.getObject();
            } else {
                //实例化properties对象
                properties = new Properties();
                //加载配置
                properties.load(new ByteArrayInputStream(bytes));
            }
            return properties;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 把请求参数的map转换成properties都系
     * 请求参数的map是一个key为string，value是String数组的map，所以不能使用apache的MapUtils.toProperties(map)进行转换
     * @param map
     * @return
     */
    public static Properties toProperties(Map<String,String[]> map){
        //1.创建返回值
        Properties properties = new Properties();
        //2.遍历map
        if(map != null && map.size() > 0){
            for(Map.Entry<String,String[]> me : map.entrySet()){
                String key = me.getKey();
                if("envName".equals(key) || "clusterNumber".equals(key) || "projectName".equals(key) || "id".equals(key)|| "serviceName".equals(key) || "projectGroup".equals(key) || "userId".equals(key)){
                    continue;
                }
                String[] value = me.getValue();
                if(value.length > 1){
                    throw new IllegalArgumentException("配置项不唯一");
                }
                properties.put(key,value[0]);
            }
        }
        //3.返回properties
        return properties;
    }

    /**
     * 把properteis转成json格式字符串
     * @param properties
     * @return
     */
    public static String toJson(Properties properties){
        try {
            String json = JSON.toJSONString(properties);
            return json;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 把json字符串转成properties对象
     * @param json
     * @return
     */
    public static Properties toProperties(String json){
        try{
            Properties properties = JSON.parseObject(json,Properties.class);
            return properties;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
