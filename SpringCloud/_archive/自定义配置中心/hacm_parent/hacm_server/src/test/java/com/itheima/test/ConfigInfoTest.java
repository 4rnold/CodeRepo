package com.itheima.test;

import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.itheima.disastertolerance.DisasterToleranceStrategy;
import com.itheima.disastertolerance.impl.FastDFSDisasterTolerance;
import com.itheima.domain.ConfigInfo;
import com.itheima.service.ConfigInfoService;
import com.itheima.utils.KryoSerializeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//public class ConfigInfoTest {
//
//    @Autowired
//    private ConfigInfoService configInfoService;
//
//    @Autowired
//    private DisasterToleranceStrategy disasterToleranceStrategy;
//
//    @Test
//    public void testFindAll(){
//        ConfigInfo configInfo = new ConfigInfo();
//        configInfo.setProjectName("tensquare");
//        //1.查询所有
//        Page page = configInfoService.findAll(configInfo,1,5);
//        //2.取出集合
//        List list = page.getContent();
//
//        System.out.println(list);
//    }
//
//
//    @Test
//    public void testFindOne(){
//        ConfigInfo configInfo = configInfoService.findById("1263157308729462784");
//        disasterToleranceStrategy.saveDfs(configInfo);
//        System.out.println(configInfo);
//    }
//
//
//
//    @Autowired
//    private FastFileStorageClient fastFileStorageClient;
//
//    @Test
//    public void testFindDfs(){
//        byte[] bytes = fastFileStorageClient.downloadFile("itheima","M00/00/00/wKgghF7OQoOAC4XNAAADEIA0waE001.yml",new DownloadByteArray());
//        if(bytes.length > 0){
//            ConfigInfo configInfo = KryoSerializeUtil.unserialize(bytes,ConfigInfo.class);
//            System.out.println(configInfo);
//        }
//    }
//}
