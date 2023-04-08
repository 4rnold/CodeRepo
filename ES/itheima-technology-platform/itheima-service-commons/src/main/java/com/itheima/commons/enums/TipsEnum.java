package com.itheima.commons.enums;

/**
 * @Class: ResultEnum
 * @Package com.itheima.commons.enums
 * @Description: 应用层操作提示
 * @Company: http://www.itheima.com/
 */
public enum TipsEnum {
    create_index_success("创建索引成功！"),
    create_index_fail("创建索引失败！"),
    delete_index_success("删除索引成功！"),
    delete_index_fail("删除索引失败！"),
    open_index_success("打开索引成功！"),
    open_index_fail("打开索引失败！"),
    close_index_success("关闭索引成功！"),
    close_index_fail("关闭索引失败！"),
    alias_index_success("索引别名设置成功！"),
    alias_index_fail("索引别名设置失败！"),
    exists_index_success("索引是否存在查询成功！"),
    exists_index_fail("引是否存在查询失败！"),


    create_doc_success("创建文档成功！"),
    create_doc_fail("创建文档失败！"),
    batch_create_doc_success("批量创建文档成功！"),
    batch_create_doc_fail("批量创建文档失败！"),
    update_doc_success("修改文档成功！"),
    update_doc_fail("修改文档失败！"),
    get_doc_success("查询文档成功！"),
    batch_get_doc_fail("批量查询文档失败！"),
    batch_get_doc_success("批量查询文档成功！"),
    get_doc_fail("查询文档失败！"),
    delete_doc_success("删除文档成功！"),
    delete_doc_fail("删除文档失败！"),
    csuggest_get_doc_fail("自动补全获取失败！"),
    csuggest_get_doc_success("自动补全获取成功！"),

    psuggest_get_doc_fail("拼写纠错获取失败！"),
    psuggest_get_doc_success("拼写纠错获取成功！"),

    tsuggest_get_doc_fail("搜索推荐获取失败！"),
    tsuggest_get_doc_success("搜索推荐获取成功！"),

    hotwords_get_doc_fail("搜索热词获取失败！"),
    hotwords_get_doc_success("搜索热词获取成功！"),
    metricagg_get_doc_fail("指标聚合处理失败！"),
    metricagg_get_doc_success("指标聚合处理成功！"),

    bucketagg_get_doc_fail("桶聚合处理失败！"),
    bucketagg_get_doc_success("桶聚合处理成功！"),


    index_default("索引创建失败！");

    private String message;

    public String getMessage() {
        return message;
    }

    TipsEnum(String message) {
        this.message = message;

    }

}
