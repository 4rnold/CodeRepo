package com.itheima.commons.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Class: CommonEntity
 * @Package com.itheima.commons.pojo
 * @Description: 公共实体类
 * @Company: http://www.itheima.com/
 */
//如果加该注解的字段为null,那么就不序列化
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonEntity implements Serializable {
    //页码
    private int pageNumber;
    //每页数据条数
    private int pageSize;
    //索引名称
    private String indexName;
    //高亮列
    private String highlight;
    //排序 DESC  ASC
    private String sortOrder;
    //排序列
    private String sortField;
    //自动补全建议列
    private String suggestFileld;
    //自动补全建议值
    private String suggestValue;
    //自动补全返回个数
    private Integer suggestCount;
    //动态查询参数封装
    Map<String, Object> map;
    //批量增加list
    private List<Map<String, Object>> list;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSuggestFileld() {
        return suggestFileld;
    }

    public void setSuggestFileld(String suggestFileld) {
        this.suggestFileld = suggestFileld;
    }

    public String getSuggestValue() {
        return suggestValue;
    }

    public void setSuggestValue(String suggestValue) {
        this.suggestValue = suggestValue;
    }

    public Integer getSuggestCount() {
        return suggestCount;
    }

    public void setSuggestCount(Integer suggestCount) {
        this.suggestCount = suggestCount;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
