package com.tangyh.lamp.activiti.exception;


import com.tangyh.basic.exception.code.BaseExceptionCode;

/**
 * 工作流错误码
 * <p>
 *
 * activiti工作流  范围：130000-131000
 *
 * @author wz
 * @date 2020-08-21 13:25
 */
public enum MyActivitiExceptionCode implements BaseExceptionCode {

    FILE_LOAD_ERR(130000, "文件读取失败！"),
    SOURCE_CREATE_ERR(130001, "资源创建失败！"),
    DATA_NOT_FOUNT(130002, "未找到相关数据！"),
    LOGIN_HAVE_NOT_AUTH(130003, "用户没有该权限！"),
    MODEL_HAS_PUBLISH(130004, "该模型已发布，请勿重复发布！"),
    MODEL_DATA_NONE(130005, "模型数据为空，请先设计流程并成功保存，再进行发布！"),
    MODEL_ERR(130006, "部署模型异常，请确认模型是否正确！"),
    MODEL_JSON_CREATE_ERR(130007, "创建流程模型JSON失败"),
    MODEL_STENCIL_LOAD_ERR(130008, "模型stencil加载失败！"),
    MODEL_EDITOR_CREATE_ERR(130009, "创建模型完善编辑器失败"),
    MODEL_NAME_NONE(130010, "模型名称不能为空"),
    MODEL_KEY_NONE(130011, "模型KEY不能为空"),
    MODEL_KEY_EXIST(130012, "模型KEY已存在"),
    EXPORT_ERR(130013, "导出失败！"),
    PROCESSINST_DELETE_ERR(130014, "流程删除失败，存在运行中的流程实例！"),
    PROCESSINST_UPDATE_ERR(130015
            , "流程操作失败，该流程已被挂起"),
    TASK_UPDATE_ERR(130016
            , "任务操作失败，该流程已被挂起"),

    ;

    private final int code;
    private String msg;

    MyActivitiExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


    public MyActivitiExceptionCode build(String msg, Object... param) {
        this.msg = String.format(msg, param);
        return this;
    }

    public MyActivitiExceptionCode param(Object... param) {
        msg = String.format(msg, param);
        return this;
    }
}
