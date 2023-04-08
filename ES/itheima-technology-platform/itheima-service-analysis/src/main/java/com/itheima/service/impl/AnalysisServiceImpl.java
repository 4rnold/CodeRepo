package com.itheima.service.impl;

import com.itheima.commons.pojo.CommonEntity;
import com.itheima.commons.thread.ResponseThreadLocal;
import com.itheima.commons.utils.SearchTools;
import com.itheima.service.AnalysisService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Class: AnalysisServiceImpl
 * @Package com.itheima.service.impl
 * @Description:分析服务接口实现
 * @Company: http://www.itheima.com/
 */
@Service
public class AnalysisServiceImpl implements AnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);
    @Resource
    private RestHighLevelClient client;

    /*
     * @Description: 获取搜索热词
     * @Method: hotWords
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.util.Map<java.lang.String,java.lang.Integer>
     *
     */
    @Override
    public Map<String, Long> hotWords(CommonEntity commonEntity) throws Exception {
        //定义返回数据
        Map<String, Long> map = new LinkedHashMap<>();
        //执行查询
        SearchResponse response = getSearchResponse(commonEntity);
        //接收数据
        Terms termsAggData = response.getAggregations().get(response.getAggregations().getAsMap().entrySet().iterator().next().getKey());
        for (Terms.Bucket entry : termsAggData.getBuckets()) {
            if (entry.getKey() != null) {
                //key为分组字段
                String key = entry.getKey().toString();
                //count数据条数
                Long count = entry.getDocCount();
                //设置到map
                map.put(key, count);
            }
        }
        return map;
    }

    /*
     * @Description: 指标聚合
     * @Method: metricAgg
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.util.Map<java.lang.Object,java.lang.Object>
     *
     */
    @Override
    public Map<Object, Object> metricAgg(CommonEntity commonEntity) throws Exception {
        return responseResultConverter(commonEntity);
    }

    /*
     * @Description:  结果转换器
     * @Method: responseResultConverter
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: java.util.Map<java.lang.Object,java.lang.Object>
     *
     */
    public Map<Object, Object> responseResultConverter(CommonEntity commonEntity) throws Exception {
        //查询公共调用
        SearchResponse response = getSearchResponse(commonEntity);
        //将数据放入到线程中
        SearchTools.setResponseThreadLocal(response);
        //定义返回数据
        Map<Object, Object> map = new HashMap<Object, Object>();
        //获取聚合数据
        Map<String, Aggregation> aggregationMap = response.getAggregations().asMap();
        //循环map获取里面的key和value
        for (Map.Entry<String, Aggregation> m : aggregationMap.entrySet()) {
            metricResultConverter(map, m);
        }
        //设置聚合后对应的数据
        commonConverter(map);
        return map;
    }

    /*
     * @Description: 公共数据处理
     * @Method: commonConverter
     * @Param: [map]
     * @Update:
     * @since: 1.0.0
     * @Return: void
     *
     */
    private void commonConverter(Map<Object, Object> map) {
        if (!CollectionUtils.isEmpty(ResponseThreadLocal.get())) {
            //从线程中获取数据
            map.put("list", ResponseThreadLocal.get());
            //清空本地线程局部变量表中的数据，防止内存泄露
            ResponseThreadLocal.clear();
        }

    }


    /*
     * @Description: 指标聚合结果转换器
     * @Method: metricResultConverter
     * @Param: [map, m]
     * @Update:
     * @since: 1.0.0
     * @Return: void
     *
     */
    private void metricResultConverter(Map<Object, Object> map, Map.Entry<String, Aggregation> m) {
        //平均值
        if (m.getValue() instanceof ParsedAvg) {
            map.put("value", ((ParsedAvg) m.getValue()).getValue());
        }
        //最大值
        if (m.getValue() instanceof ParsedMax) {
            map.put("value", ((ParsedMax) m.getValue()).getValue());
        }
        //最小值
        if (m.getValue() instanceof ParsedMin) {
            map.put("value", ((ParsedMin) m.getValue()).getValue());
        }
        //求和
        if (m.getValue() instanceof ParsedSum) {
            map.put("value", ((ParsedSum) m.getValue()).getValue());
        }
        //不重复的值
        if (m.getValue() instanceof ParsedCardinality) {
            map.put("value", ((ParsedCardinality) m.getValue()).getValue());
        }
        //状态统计
        if (m.getValue() instanceof ParsedStats) {
            map.put("count", ((ParsedStats) m.getValue()).getCount());
            map.put("min", ((ParsedStats) m.getValue()).getMin());
            map.put("max", ((ParsedStats) m.getValue()).getMax());
            map.put("avg", ((ParsedStats) m.getValue()).getAvg());
            map.put("sum", ((ParsedStats) m.getValue()).getSum());
        }
        //扩展统计
        if (m.getValue() instanceof ParsedExtendedStats) {
            map.put("count", ((ParsedExtendedStats) m.getValue()).getCount());
            map.put("min", ((ParsedExtendedStats) m.getValue()).getMin());
            map.put("max", ((ParsedExtendedStats) m.getValue()).getMax());
            map.put("avg", ((ParsedExtendedStats) m.getValue()).getAvg());
            map.put("sum", ((ParsedExtendedStats) m.getValue()).getSum());
            map.put("sum_of_squares", ((ParsedExtendedStats) m.getValue()).getSumOfSquares());
            map.put("variance", ((ParsedExtendedStats) m.getValue()).getVariance());
            map.put("std_deviation", ((ParsedExtendedStats) m.getValue()).getStdDeviation());
            map.put("upper", ((ParsedExtendedStats) m.getValue()).getStdDeviationBound(ExtendedStats.Bounds.UPPER));
            map.put("lower", ((ParsedExtendedStats) m.getValue()).getStdDeviationBound(ExtendedStats.Bounds.LOWER));

        }
        //百分位度量
        if (m.getValue() instanceof ParsedTDigestPercentiles) {
            for (Iterator<Percentile> iterator = ((ParsedTDigestPercentiles) m.getValue()).iterator(); iterator.hasNext(); ) {
                Percentile p = iterator.next();
                map.put(p.getPercent(), p.getValue());
            }
        }

        //百分位等级
        if (m.getValue() instanceof ParsedTDigestPercentileRanks) {
            for (Iterator<Percentile> iterator = ((ParsedTDigestPercentileRanks) m.getValue()).iterator(); iterator.hasNext(); ) {
                Percentile p = iterator.next();
                map.put(p.getValue(), p.getPercent());
            }
        }
    }

    /*
     * @Description: 获取查询响应
     * @Method: getSearchResponse
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: org.elasticsearch.action.search.SearchResponse
     *
     */
    private SearchResponse getSearchResponse(CommonEntity commonEntity) throws Exception {
        //定义查询请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引查询
        searchRequest.indices(commonEntity.getIndexName());
        //定义资源查询构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //获取解析器
        XContentParser parser = SearchTools.getXContentParser(commonEntity);
        //将parse注册到构建器
        searchSourceBuilder.parseXContent(parser);
        //设置查询请求
        searchRequest.source(searchSourceBuilder);
        //执行查询
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse;
    }


    /*
     * @Description: 指标聚合
     * ParsedExtendedStats和ParsedStats因为是通过getName做流程控制，所以不存在if中的ParsedExtendedStats子类也走ParsedStats父类的逻辑
     * @Method: metricResultConverter2
     * @Param: [map, m]
     * @Update:
     * @since: 1.0.0
     * @Return: void
     *
     */
    private void metricResultConverter_back(Map<Object, Object> map, Map.Entry<String, Aggregation> m) {
        switch (m.getValue().getClass().getName()) {
            //平均值
            case "org.elasticsearch.search.aggregations.metrics.ParsedAvg":
                map.put("value", ((ParsedAvg) m.getValue()).getValue());
                break;
            //最大值
            case "org.elasticsearch.search.aggregations.metrics.ParsedMax":
                map.put("value", ((ParsedMax) m.getValue()).getValue());
                break;
            //最小值
            case "org.elasticsearch.search.aggregations.metrics.ParsedMin":
                map.put("value", ((ParsedMin) m.getValue()).getValue());
                break;
            //求和
            case "org.elasticsearch.search.aggregations.metrics.ParsedSum":
                map.put("value", ((ParsedSum) m.getValue()).getValue());
                break;
            //不重复的值
            case "org.elasticsearch.search.aggregations.metrics.ParsedCardinality":
                map.put("value", ((ParsedCardinality) m.getValue()).getValue());
                break;
            //扩展统计
            case "org.elasticsearch.search.aggregations.metrics.ParsedExtendedStats":
                map.put("count", ((ParsedExtendedStats) m.getValue()).getCount());
                map.put("min", ((ParsedExtendedStats) m.getValue()).getMin());
                map.put("max", ((ParsedExtendedStats) m.getValue()).getMax());
                map.put("avg", ((ParsedExtendedStats) m.getValue()).getAvg());
                map.put("sum", ((ParsedExtendedStats) m.getValue()).getSum());
                map.put("sum_of_squares", ((ParsedExtendedStats) m.getValue()).getSumOfSquares());
                map.put("variance", ((ParsedExtendedStats) m.getValue()).getVariance());
                map.put("std_deviation", ((ParsedExtendedStats) m.getValue()).getStdDeviation());
                map.put("upper", ((ParsedExtendedStats) m.getValue()).getStdDeviationBound(ExtendedStats.Bounds.UPPER));
                map.put("lower", ((ParsedExtendedStats) m.getValue()).getStdDeviationBound(ExtendedStats.Bounds.LOWER));
                break;
            //状态统计
            case "org.elasticsearch.search.aggregations.metrics.ParsedStats":
                map.put("count", ((ParsedStats) m.getValue()).getCount());
                map.put("min", ((ParsedStats) m.getValue()).getMin());
                map.put("max", ((ParsedStats) m.getValue()).getMax());
                map.put("avg", ((ParsedStats) m.getValue()).getAvg());
                map.put("sum", ((ParsedStats) m.getValue()).getSum());
                break;
            //百分位度量
            case "org.elasticsearch.search.aggregations.metrics.ParsedTDigestPercentiles":
                for (Iterator<Percentile> iterator = ((ParsedTDigestPercentiles) m.getValue()).iterator(); iterator.hasNext(); ) {
                    Percentile p = iterator.next();
                    map.put(p.getPercent(), p.getValue());
                }
                break;
            //百分位等级
            case "org.elasticsearch.search.aggregations.metrics.ParsedTDigestPercentileRanks":
                for (Iterator<Percentile> iterator = ((ParsedTDigestPercentileRanks) m.getValue()).iterator(); iterator.hasNext(); ) {
                    Percentile p = iterator.next();
                    map.put(p.getValue(), p.getPercent());
                }
                break;
            default:
                break;
        }
    }








}