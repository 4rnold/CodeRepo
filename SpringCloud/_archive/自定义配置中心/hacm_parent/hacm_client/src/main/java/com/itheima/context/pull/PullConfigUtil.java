package com.itheima.context.pull;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Properties;

/**
 * 从服务端拉取最新配置的工具类
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class PullConfigUtil {

    private static String configHost ;//应用配置管理平台的IP地址

    private static String configPort;//应用配置管理平台的端口号

    private static String configId;//配置的唯一标识

    //静态代码块给成员变量赋值
    static {
        InputStream in = null;
        String[] fileNames = new String[]{"/bootstrap.yml","/application.yml","/application.properties"};
        try{
            //1.读取配置文件
            for(String fileName : fileNames){
                try{
                    //读取微服务的配置文件
                    in = PullConfigUtil.class.getResourceAsStream(fileName);
                    //判断，取到了配置
                    if(in.available() > 0){
                        break;
                    }
                }catch (Exception ex){
                    continue;
                }
            }
            //2.使用配置文件的内容给成员变量赋值
            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(new InputStreamResource(in));
            Properties properties = yamlPropertiesFactoryBean.getObject();
            configHost = properties.getProperty("config.host");
            configPort = properties.getProperty("config.port");
            configId = properties.getProperty("config.id");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从服务端拉取最新配置的方法
     * @return
     */
    public static Properties findConfig(){
        try {
            //1.定义请求消息头的信息
            HttpHeaders requestHeaders = new HttpHeaders();
            //2.设置请求消息头内容
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //3.创建请求体对象
            MultiValueMap<String,String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("id",configId);
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity(requestBody,requestHeaders);
            //4.创建发送请求的对象
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://"+configHost+":"+configPort+"/config/refreshConfig.do";
            String json = restTemplate.postForObject(url,requestEntity,String.class);
            //5.把Json格式数据转成properties
            Properties properties = JSON.parseObject(json,Properties.class);
            return properties;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
