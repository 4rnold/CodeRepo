package com.arnold.core.response;

import com.arnold.common.enums.ResponseCode;
import com.arnold.common.utils.JSONUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.http.*;
import lombok.Data;
import org.asynchttpclient.Response;

@Data
public class GatewayResponse {

    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    private HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();

    private String content;

    private HttpResponseStatus httpResponseStatus;

    //异步？
    private Response futureResponse;

    public GatewayResponse() {

    }

    //putheader
    public void putHeader(CharSequence name, CharSequence value) {
        responseHeaders.add(name, value);
    }

    //buildGatewayResponse
    public static GatewayResponse buildGatewayResponse(Response response) {
        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setFutureResponse(response);
        gatewayResponse.setHttpResponseStatus(HttpResponseStatus.valueOf(response.getStatusCode()));
        return gatewayResponse;
    }

    //返回一个Json类型的响应信息，失败时使用
    public static GatewayResponse buildGatewayResponse(ResponseCode code, Object... args) {
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, code.getStatus().code());
        objectNode.put(JSONUtil.CODE, code.getCode());
        objectNode.put(JSONUtil.MESSAGE, code.getMessage());

        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setHttpResponseStatus(code.getStatus());
        gatewayResponse.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        gatewayResponse.setContent(JSONUtil.toJSONString(objectNode));

        return gatewayResponse;
    }

    /**
     * 返回一个Json类型的响应信息，成功时使用
     */
    public static GatewayResponse buildGatewayResponse(Object data) {
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, ResponseCode.SUCCESS.getStatus().code());
        objectNode.put(JSONUtil.CODE, ResponseCode.SUCCESS.getCode());
        objectNode.putPOJO(JSONUtil.DATA, data);

        GatewayResponse gatewayResponse = new GatewayResponse();
        gatewayResponse.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        gatewayResponse.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        gatewayResponse.setContent(JSONUtil.toJSONString(objectNode));
        return gatewayResponse;

    }
}
