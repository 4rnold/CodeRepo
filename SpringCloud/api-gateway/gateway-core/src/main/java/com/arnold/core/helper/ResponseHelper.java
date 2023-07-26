package com.arnold.core.helper;

import com.arnold.common.constants.BasicConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.core.context.IContext;
import com.arnold.core.response.GatewayResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import java.util.Objects;

/**
 * 响应的辅助类
 */
public class ResponseHelper {

	/**
	 * 获取响应对象
	 */
	public static FullHttpResponse getHttpResponse(ResponseCode responseCode) {
		GatewayResponse gatewayResponse = GatewayResponse.buildGatewayResponse(responseCode);
		DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.INTERNAL_SERVER_ERROR,
				Unpooled.wrappedBuffer(gatewayResponse.getContent().getBytes()));

		httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
		httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
		return httpResponse;
	}

	/**
	 * 通过上下文对象和Response对象 构建FullHttpResponse
	 * 1.从GatewayResponse获取nettyResponseBuf
	 * 2.nettyResponseBuf转为nettyHttpResponse
	 */
	private static FullHttpResponse getHttpResponse(IContext ctx, GatewayResponse gatewayResponse) {
		ByteBuf nettyResponseBuf;
		//从GatewayResponse获取nettyResponseBuf
		if(Objects.nonNull(gatewayResponse.getFutureResponse())) {
			//将nio的buffer转为netty的buf
			nettyResponseBuf = Unpooled.wrappedBuffer(gatewayResponse.getFutureResponse()
					.getResponseBodyAsByteBuffer());
		}
		else if(gatewayResponse.getContent() != null) {//什么情况使用content，什么情况使用futureResponse
			nettyResponseBuf = Unpooled.wrappedBuffer(gatewayResponse.getContent().getBytes());
		}
		else {
			nettyResponseBuf = Unpooled.wrappedBuffer(BasicConst.BLANK_SEPARATOR_1.getBytes());
		}

		//nettyResponseBuf转为nettyHttpResponse
		if(Objects.isNull(gatewayResponse.getFutureResponse())) {
			DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
					gatewayResponse.getHttpResponseStatus(),
					 nettyResponseBuf);
			httpResponse.headers().add(gatewayResponse.getResponseHeaders());
			httpResponse.headers().add(gatewayResponse.getExtraResponseHeaders());
			httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
			return httpResponse;
		} else {
			gatewayResponse.getFutureResponse().getHeaders().add(gatewayResponse.getExtraResponseHeaders());

			DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
					 HttpResponseStatus.valueOf(gatewayResponse.getFutureResponse().getStatusCode()),
					 nettyResponseBuf);
			httpResponse.headers().add(gatewayResponse.getFutureResponse().getHeaders());
			return httpResponse;
		}
	}


	/**
	 * 写回响应信息方法
	 */
	public static void writeResponse(IContext context) {

		//	释放资源
		context.releaseRequest();

		if(context.isWritten()) {
			//	1：第一步构建响应对象，并写回数据
			FullHttpResponse httpResponse = ResponseHelper.getHttpResponse(context, (GatewayResponse)context.getResponse());
			if(!context.isKeepAlive()) {
				context.getChannelHandlerContext()
					.writeAndFlush(httpResponse)
					.addListener(ChannelFutureListener.CLOSE);///不是isKeepAlive就每次都close连接。
			}
			//	长连接：
			else {
				httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				context.getChannelHandlerContext().writeAndFlush(httpResponse);
			}
			//	2:	设置写回结束状态为： COMPLETED
			context.isCompleted();
		}
		else if(context.isCompleted()){
			context.invokeCompletedCallBack();
		}

	}

}
