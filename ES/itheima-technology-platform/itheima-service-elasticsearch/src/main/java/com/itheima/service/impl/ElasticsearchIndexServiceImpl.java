package com.itheima.service.impl;

import com.itheima.commons.pojo.CommonEntity;
import com.itheima.service.ElasticsearchIndexService;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Class: ElasticsearchIndexServiceImpl
 * @Package com.itheima.service.impl
 * @Description:索引操作接口实现类
 * @Company: http://www.itheima.com/
 */
@Service
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {
    @Resource
    private RestHighLevelClient client;
    /*
    * @Description: 创建索引+映射
    * @Method: addIndexAndMapping
    * @Param: [commonEntity]
    * @Update:
    * @since: 1.0.0
    * @Return: boolean
    *
    */
    @Override
    public boolean addIndexAndMapping(CommonEntity commonEntity) throws Exception {
        boolean  flag=false;
        //创建索引请求
        CreateIndexRequest request=new CreateIndexRequest(commonEntity.getIndexName());
        //获取下游业务参数
        Map<String,Object> map =commonEntity.getMap();
        //循环参数
        for(Map.Entry<String,Object> entry:map.entrySet()){
            //设置settings参数
            if("settings".equals(entry.getKey()) && entry.getValue() instanceof  Map && ((Map)entry.getValue()).size()>0){
                request.settings(((Map)entry.getValue()));
            }
            //设置mapping参数
            if("mapping".equals(entry.getKey()) && entry.getValue() instanceof  Map && ((Map)entry.getValue()).size()>0){
                request.mapping(((Map)entry.getValue()));
            }
        }
        //创建索引操作客户端
        IndicesClient indicesClient=client.indices();
        //创建响应对象
        CreateIndexResponse response=indicesClient.create(request,RequestOptions.DEFAULT);
        flag=response.isAcknowledged();
        return flag;
    }
}
