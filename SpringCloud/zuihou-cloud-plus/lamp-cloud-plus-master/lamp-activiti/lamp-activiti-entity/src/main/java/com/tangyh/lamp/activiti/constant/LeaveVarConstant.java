package com.tangyh.lamp.activiti.constant;

/**
 * 请假流程相关流程变量
 *
 * @author wz
 * @date 2020-08-07
 */
public final class LeaveVarConstant {
    private LeaveVarConstant() {
    }

    /**
     * 流程变量KEY
     */
    public static final String LEAVE_USER = "请假人员";
    public static final String START_TIME = "开始时间";
    public static final String END_TIME = "结束时间";
    public static final String LEAVE_LONG = "请假时长";
    public static final String LEAVE_TYPE = "请假类型";
    public static final String LEAVE_REASON = "请假事由";
    public static final String USERNAME = "USERNAME";
    public static final String RESULT = "RESULT";

    /**
     * 信息常量
     */
    public static final String RESULT_MSG = "审批结果";
}
