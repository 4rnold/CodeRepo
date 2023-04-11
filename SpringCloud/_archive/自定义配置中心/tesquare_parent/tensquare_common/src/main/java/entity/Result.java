package entity;

import java.io.Serializable;

/**
 * 用于响应浏览器的结果集信息
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class Result implements Serializable{

    private boolean flag;//是否成功
    private int code;//状态码
    private String message;//提示信息
    private Object data;//响应数据（此信息不是什么时候都有，增删改就没有，查询的时候就有）

    public Result() {
    }

    /**
     * 针对增删改
     * @param flag
     * @param code
     * @param message
     */
    public Result(boolean flag, int code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    /**
     * 针对查询的
     * @param flag
     * @param code
     * @param message
     * @param data
     */
    public Result(boolean flag, int code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
