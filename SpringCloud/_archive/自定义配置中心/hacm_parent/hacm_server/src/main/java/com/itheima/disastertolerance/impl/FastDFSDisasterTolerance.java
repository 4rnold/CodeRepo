package com.itheima.disastertolerance.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.itheima.conditions.FastDFSCondition;
import com.itheima.disastertolerance.DisasterToleranceStrategy;
import com.itheima.domain.ConfigInfo;
import com.itheima.utils.KryoSerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS的容灾实现
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
@Conditional(FastDFSCondition.class)
public class FastDFSDisasterTolerance implements DisasterToleranceStrategy {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${fdfs.file.suffix}")
    private String suffix;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String PREFIXKEY = "fastdfs_";

    private static final String SPLITKEY = "_heimaconfig_";

    @Override
    public void saveDfs(ConfigInfo configInfo) {
        InputStream in = null;
        try{
            //判断是否已经在fastdfs中保存过了，如果保存过，应该删除
            this.removeDfs(configInfo.getId());
            //把configInfo读入到字节数组输入流中
            in = new ByteArrayInputStream(KryoSerializeUtil.serialize(configInfo));
            //把流的大小转成Long类型
            Long fileSize = new Long(in.available());
            //上传文件
            StorePath storePath = fastFileStorageClient.uploadFile(in,fileSize,suffix,null);
            //把返回的结果存入redis中
            redisTemplate.opsForValue().set(PREFIXKEY.concat(configInfo.getId()),storePath.getGroup()+SPLITKEY+storePath.getPath());
            System.out.println(storePath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放流资源
            if(in != null){
                try {
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void removeDfs(String id) {
        //1.根据id，组装redis中的key
        String key = PREFIXKEY.concat(id);
        //2.使用key前往redis获取信息
        String value = (String)redisTemplate.opsForValue().get(key);
        //3.判断value是否有值
        if(!StringUtils.isEmpty(value)){
            //4.分隔value
            String group = value.split(SPLITKEY)[0];
            String path = value.split(SPLITKEY)[1];
            //5.删除文件
            fastFileStorageClient.deleteFile(group,path);
        }
    }

    @Override
    public ConfigInfo findDfs(String id) {
        //定义返回值
        ConfigInfo configInfo = null;
        //1.根据id，组装redis中的key
        String key = PREFIXKEY.concat(id);
        //2.使用key前往redis获取信息
        String value = (String)redisTemplate.opsForValue().get(key);
        //3.判断value是否有值
        if(!StringUtils.isEmpty(value)){
            //4.分隔value
            String group = value.split(SPLITKEY)[0];
            String path = value.split(SPLITKEY)[1];
            //5.获取文件字节数组
            byte[] bytes = fastFileStorageClient.downloadFile(group,path,new DownloadByteArray());
            //6.把它转换成ConfigInfo对象
            if(bytes.length > 0) {
                configInfo = KryoSerializeUtil.unserialize(bytes, ConfigInfo.class);
            }
        }
        return configInfo;
    }
}
