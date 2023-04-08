package com.itheima.service.impl;

import com.itheima.commons.pojo.CommonEntity;
import com.itheima.commons.utils.SearchTools;
import com.itheima.service.ElasticsearchDocumentService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Class: ElasticsearchDocumentServiceImpl
 * @Package com.itheima.service.impl
 * @Description: 终搜检索接口实现
 * @Company: http://www.itheima.com/
 */
@Service
public class ElasticsearchDocumentServiceImpl implements ElasticsearchDocumentService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchDocumentServiceImpl.class);
    @Resource
    private RestHighLevelClient client;

    /*
     * @Description:  批量新增
     * @Method: bulkAndDoc
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: org.elasticsearch.rest.RestStatus
     *
     */
    @Override
    public RestStatus bulkAndDoc(CommonEntity commonEntity) throws Exception {
        //构建批量新增请求
        BulkRequest bulkRequest = new BulkRequest(commonEntity.getIndexName());
        //循环下游业务文档数据
        for (int i = 0; i < commonEntity.getList().size(); i++) {
            bulkRequest.add(new IndexRequest().source(XContentType.JSON, SearchTools.mapToObjectGroup(commonEntity.getList().get(i))));
        }
        //开始执行批量新增操作
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulkResponse.status();
    }

    /*
     * @Description: 拼写纠错
     * @Method: pSuggest
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.lang.String
     *
     */
    @Override
    public String pSuggest(CommonEntity commonEntity) throws Exception {
        //定义返回
        String pSuggestString = new String();
        //定义短语建议器的构建器
        PhraseSuggestionBuilder phraseSuggestionBuilder = new PhraseSuggestionBuilder(commonEntity.getSuggestFileld());
        //设置搜索关键字
        phraseSuggestionBuilder.text(commonEntity.getSuggestValue());
        //数量匹配
        phraseSuggestionBuilder.size(1);
        //定义返回字段
        SearchRequest searchRequest = new SearchRequest().indices(commonEntity.getIndexName()).source(new SearchSourceBuilder().sort(new ScoreSortBuilder().order(SortOrder.DESC)).suggest(new SuggestBuilder().addSuggestion("czbk-suggest", phraseSuggestionBuilder)));
        //定义查找响应
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //定义短语建议对象
        PhraseSuggestion phraseSuggestion = response.getSuggest().getSuggestion("czbk-suggest");
        //获取返回数据
        List<PhraseSuggestion.Entry.Option> optionList = phraseSuggestion.getEntries().get(0).getOptions();
        //从optionList取出结果
        if (!CollectionUtils.isEmpty(optionList)) {

            pSuggestString = optionList.get(0).getText().toString();
        }
        return pSuggestString;
    }

    /*
     * @Description: 搜索推荐
     * @Method: tSuggest
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.lang.String
     *
     */
    @Override
    public String tSuggest(CommonEntity commonEntity) throws Exception {
        //定义返回
        String tSuggestString = new String();
        //定义词条建议器的构建器
        TermSuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion(commonEntity.getSuggestFileld());
        //定义搜索关键字
        termSuggestionBuilder.text(commonEntity.getSuggestValue());
        //设置分词
        termSuggestionBuilder.analyzer("ik_smart");
        //定义查询长度
        termSuggestionBuilder.minWordLength(2);
        //设置查找算法
        termSuggestionBuilder.stringDistance(TermSuggestionBuilder.StringDistanceImpl.NGRAM);
        //定义返回字段
        SearchRequest searchRequest = new SearchRequest().indices(commonEntity.getIndexName()).source(new SearchSourceBuilder().sort(new ScoreSortBuilder().order(SortOrder.DESC)).suggest(new SuggestBuilder().addSuggestion("czbk-suggest", termSuggestionBuilder)));
        //定义查找响应
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //定义term建议对象
        TermSuggestion termSuggestion = response.getSuggest().getSuggestion("czbk-suggest");
        //获取返回数据
        List<TermSuggestion.Entry.Option> optionList = termSuggestion.getEntries().get(0).getOptions();
        //从optionList取出结果
        if (!CollectionUtils.isEmpty(optionList)) {
            tSuggestString = optionList.get(0).getText().toString();

        }
        return tSuggestString;
    }

    /*
     * @Description: 自动补全
     * @Method: cSuggest
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.util.List<java.lang.String>
     *
     */
    @Override
    public List<String> cSuggest(CommonEntity commonEntity) throws Exception {
        //定义返回
        List<String> suggestList = new ArrayList<>();
        //定义自动完成构建器
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion(commonEntity.getSuggestFileld());
        //定义搜索关键字
        completionSuggestionBuilder.prefix(commonEntity.getSuggestValue());
        //去重
        completionSuggestionBuilder.skipDuplicates(true);
        //获取建议条数
        completionSuggestionBuilder.size(commonEntity.getSuggestCount());
        //定义返回字段
        SearchRequest searchRequest = new SearchRequest().indices(commonEntity.getIndexName()).source(new SearchSourceBuilder().sort(new ScoreSortBuilder().order(SortOrder.DESC)).suggest(new SuggestBuilder().addSuggestion("czbk-suggest", completionSuggestionBuilder)));
        //定义查找响应
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //定义完成建议对象
        CompletionSuggestion completionSuggestion = response.getSuggest().getSuggestion("czbk-suggest");
//获取返回数据
        List<CompletionSuggestion.Entry.Option> optionList = completionSuggestion.getEntries().get(0).getOptions();
        //从optionList取出结果
        if (!CollectionUtils.isEmpty(optionList)) {
            optionList.forEach(item -> {
                suggestList.add(item.getText().toString());
            });
        }
        return suggestList;
    }

    /*
     * @Description: 结构化搜索
     * @Method: termQuery
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: org.elasticsearch.action.search.SearchResponse
     *
     */
    @Override
    public SearchResponse termQuery(CommonEntity commonEntity) throws Exception {
        //构建查询响应
        SearchResponse response = null;
        //构建查询请求
        SearchRequest searchRequest = new SearchRequest(commonEntity.getIndexName());
        //定义查询构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        //定义查询解析器
        XContentParser parser = SearchTools.getXContentParser(commonEntity);
        //将parser对象放到构建器中
        searchSourceBuilder.parseXContent(parser);
        //高亮设置
        searchSourceBuilder.highlighter(SearchTools.getHighlightBuilder(commonEntity.getHighlight()));
        //获取下游业务的页码
        int pageNumber = commonEntity.getPageNumber();
        //获取下游业务每页显示的数据量（条数）
        int pageSize = commonEntity.getPageSize();
        //计算查询下标
        int dest = (pageNumber - 1) * pageSize;
        //设置查询下标到构建器
        searchSourceBuilder.from(dest);
        //设置查询的每页数量
        searchSourceBuilder.size(pageSize);
        //排序
        sortForQuery(commonEntity, searchSourceBuilder);
        //将查询构建器赋值给查询请求
        searchRequest.source(searchSourceBuilder);
        //执行远程查询
        response = client.search(searchRequest, RequestOptions.DEFAULT);
        //处理高亮
        SearchTools.setHighResultForCleintUI(response, commonEntity.getHighlight());
        return response;
    }

    /*
     * @Description: 排序
     * @Method: sortForQuery
     * @Param: [commonEntity, searchSourceBuilder]
     * @Update:
     * @since: 1.0.0
     * @Return: void
     *
     */
    private void sortForQuery(CommonEntity commonEntity, SearchSourceBuilder searchSourceBuilder) {
        //获取下游业务排序列名称
        String sortField = commonEntity.getSortField();
        if (StringUtils.isNotEmpty(sortField)) {
            //定义排序器
            SortOrder sortOrder = SearchTools.getSortOrder(commonEntity.getSortOrder());
            //执行排序
            searchSourceBuilder.sort(new FieldSortBuilder(sortField).order(sortOrder));
        }
    }

    /*
     * @Description: 全文检索
     * @Method: matchQuery
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: org.elasticsearch.action.search.SearchResponse
     *
     */
    @Override
    public SearchResponse matchQuery(CommonEntity commonEntity) throws Exception {
        //构建查询响应
        SearchResponse response = null;
        //构建查询请求
        SearchRequest searchRequest = new SearchRequest(commonEntity.getIndexName());
        //定义查询构建器,trackTotalHits必须设置true，如果使用默认值将缺失1w条后面的所有数据
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        //获取下游业务查询条件
        getClientConditions(commonEntity, searchSourceBuilder);
        //高亮设置
        searchSourceBuilder.highlighter(SearchTools.getHighlightBuilder(commonEntity.getHighlight()));
        //分页设置
        int pageNumber = commonEntity.getPageNumber();
        //获取每页显示的数量
        int pageSize = commonEntity.getPageSize();
        //定义查询下标
        int dest = (pageNumber - 1) * pageSize;
        //每页数量
        searchSourceBuilder.size(pageSize);
        //设置下标
        searchSourceBuilder.from(dest);
        //将构建器对象放入到请求对象中
        searchRequest.source(searchSourceBuilder);
        //开始执行远程查询
        response = client.search(searchRequest, RequestOptions.DEFAULT);
        //渲染高亮模式
        SearchTools.setHighResultForCleintUI(response, commonEntity.getHighlight());
        return response;
    }

    /*
     * @Description: 获取下游业务查询条件
     * @Method: getClientConditions
     * @Param: [commonEntity, searchSourceBuilder]
     * @Update:
     * @since: 1.0.0
     * @Return: void
     *
     */
    private void getClientConditions(CommonEntity commonEntity, SearchSourceBuilder searchSourceBuilder) {
        //循环下游业务查询条件
        for (Map.Entry<String, Object> m : commonEntity.getMap().entrySet()) {
            if (StringUtils.isNotEmpty(m.getKey()) && m.getValue() != null) {
                String key = m.getKey();
                String value = String.valueOf(m.getValue());
                //构造DSL请求体中的query
                searchSourceBuilder.query(QueryBuilders.matchQuery(key, value));
                logger.info("search for the keyword:" + value);
            }
        }
    }
}
