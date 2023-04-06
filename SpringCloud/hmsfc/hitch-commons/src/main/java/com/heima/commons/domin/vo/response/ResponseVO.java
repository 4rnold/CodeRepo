package com.heima.commons.domin.vo.response;


import com.heima.commons.domin.po.PO;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.enums.ResponseState;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.LocalCollectionUtils;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class ResponseVO<T> implements Serializable {


    public ResponseVO() {

    }

    private static Object[] transformVO(Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (null != list && list.size() > 0 && list.get(0) instanceof PO) {
                List<VO> voList = CommonsUtils.toVO(list);
                return voList.toArray();
            } else {
                return list.toArray();
            }
        }

        if (obj instanceof PO) {
            obj = CommonsUtils.toVO((PO) obj);
            return new Object[]{obj};
        }
        return new Object[]{obj};
    }


    public static ResponseVO build(ResponseState state) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(state.getCode());
        responseVO.setMessage(state.getErrorMsg());
        return responseVO;
    }


    public static ResponseVO success(Object data) {
        ResponseVO responseVO = build(ResponseState.SUCCESS);
        responseVO.setData(LocalCollectionUtils.toList(transformVO(data)));
        return responseVO;
    }

    public static ResponseVO success(Object data, String message) {
        ResponseVO responseVO = build(ResponseState.SUCCESS);
        responseVO.setMessage(message);
        responseVO.setData(LocalCollectionUtils.toList(transformVO(data)));
        return responseVO;
    }

    public static ResponseVO error(String message) {
        ResponseVO responseVO = build(ResponseState.ERROR);
        responseVO.setMessage(message);
        return responseVO;
    }

    public static ResponseVO error(BusinessRuntimeException exception) {
        ResponseVO responseVO = build(ResponseState.ERROR);
        responseVO.setMessage(exception.getMessage());
        responseVO.setCode(exception.getBusinessError().getCode());
        return responseVO;
    }

    public static ResponseVO error(BusinessErrors errors) {
        ResponseVO responseVO = build(ResponseState.ERROR);
        responseVO.setMessage(errors.getMsg());
        responseVO.setCode(errors.getCode());
        return responseVO;
    }


    public static ResponseVO unknown(String message) {
        ResponseVO responseVO = build(ResponseState.UNKNOWN);
        responseVO.setMessage(message);
        return responseVO;
    }


    @ApiModelProperty(value = "错误码")
    private int code;
    @ApiModelProperty(value = "返回数据")
    private List<Object> data = null;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
