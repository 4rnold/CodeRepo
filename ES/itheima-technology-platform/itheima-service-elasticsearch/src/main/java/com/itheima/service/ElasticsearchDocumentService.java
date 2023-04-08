package com.itheima.service;

import com.itheima.commons.pojo.CommonEntity;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;

import java.util.List;

/**
 * @Class: ElasticsearchDocumentService
 * @Package com.itheima.service
 * @Description: 终搜平台检索接口
 * @Company: http://www.itheima.com/
 */
public interface ElasticsearchDocumentService {
    //全文检索方法
    public SearchResponse matchQuery(CommonEntity commonEntity) throws Exception;

    //结构化查询
    public SearchResponse termQuery(CommonEntity commonEntity) throws Exception;

    //批量新增文档
    public RestStatus bulkAndDoc(CommonEntity commonEntity) throws Exception;

    //自动补全(完成建议)
    public List<String> cSuggest(CommonEntity commonEntity) throws Exception;

    //拼写纠错
    public String pSuggest(CommonEntity commonEntity) throws Exception;

    //搜索推荐
    public String tSuggest(CommonEntity commonEntity) throws Exception;
}
