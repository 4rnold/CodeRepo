package net.xdclass.strategy;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

public class CustomComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
    /**
     *
     * @param dataSourceNames 数据源集合
     *                      在分库时值为所有分片库的集合 databaseNames
     *                      分表时为对应分片库中所有分片表的集合 tablesNames
     *
     * @param shardingValue  分片属性，包括
     *                                  logicTableName 为逻辑表，
     *                                  columnName 分片健（字段），
     *                                  value 为从 SQL 中解析出的分片健的值
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> dataSourceNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {

        // 得到每个分片健对应的值
        Collection<Long> orderIdValues = this.getShardingValue(complexKeysShardingValue, "id");
        Collection<Long> userIdValues = this.getShardingValue(complexKeysShardingValue, "user_id");

        List<String> shardingSuffix = new ArrayList<>();
        // 对两个分片健取模的方式 product_order_0_0、product_order_0_1、product_order_1_0、product_order_1_1
        for (Long userId : userIdValues) {
            for (Long orderId : orderIdValues) {
                String suffix = userId % 2 + "_" + orderId % 2;
                for (String databaseName : dataSourceNames) {
                    if (databaseName.endsWith(suffix)) {
                        shardingSuffix.add(databaseName);
                    }
                }
            }
        }
        return shardingSuffix;
    }

    /**
     * shardingValue  分片属性，包括
     * logicTableName 为逻辑表，
     * columnNameAndShardingValuesMap 存储多个分片健 包括key-value
     * key：分片key，id和user_id
     * value：分片value，66和99
     *
     * @return shardingValues 集合
     */
    private Collection<Long> getShardingValue(ComplexKeysShardingValue<Long> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = shardingValues.getColumnNameAndShardingValuesMap();

        if (columnNameAndShardingValuesMap.containsKey(key)) {
            valueSet.addAll(columnNameAndShardingValuesMap.get(key));
        }
        return valueSet;
    }

}
