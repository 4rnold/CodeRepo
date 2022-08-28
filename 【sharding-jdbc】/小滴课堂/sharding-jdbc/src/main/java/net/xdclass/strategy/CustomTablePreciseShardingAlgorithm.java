package net.xdclass.strategy;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

public class CustomTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

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
    public String doSharding(Collection<String> dataSourceNames, PreciseShardingValue<Long> preciseShardingValue) {

        for(String datasourceName : dataSourceNames){

            String value = preciseShardingValue.getValue() % dataSourceNames.size() + "";
            //product_order_0
            if(datasourceName.endsWith(value)){
                return  datasourceName;
            }
        }
        return null;
    }
}
