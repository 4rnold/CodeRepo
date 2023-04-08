package com.itheima.commons.enums;

/**
 * @Class: ResultEnum
 * @Package com.itheima.commons.enums
 * @Description: 操作提示枚举类
 * @Company: http://www.itheima.com/
 */
public enum ResultEnum {
    success("200", "操作成功！"),
    param_isnull("-400", "参数为空"),
    error("-402", "操作失败！"),
    server_error("-500", "服务异常"),
    data_existent("-504", "数据不存在"),
    result_empty("-000", "查询内容为空"),

    NOT_SYSTEM_API("404", "不是系统指定api"),
    REPEAT("666", "数据已存在"),
    HTTP_ERROR("-405", "请求异常");
    private String code;
    private String decs;

    public String getCode() {
        return code;
    }

    public String getDecs() {
        return decs;
    }

    ResultEnum(String code, String decs) {
        this.code = code;
        this.decs = decs;
    }

}
