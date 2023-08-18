package com.arnold.core.context;

import com.arnold.common.rule.Rule;
import com.arnold.core.request.GatewayRequest;
import com.arnold.core.response.GatewayResponse;
import io.micrometer.core.instrument.Timer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GatewayContext extends BasicContext {

    public GatewayRequest request;

    public GatewayResponse response;

    public Rule rule;

    private int currentRetryTimes;

    private boolean isGray;

    private Timer.Sample timerSample;

    public GatewayContext(String protocol, ChannelHandlerContext ctx, boolean keepAlive,
                          GatewayRequest gateWayRequest, Rule rule, int currentRetryTimes) {
        super(protocol, ctx, keepAlive);
        this.request = gateWayRequest;
        this.rule = rule;
        this.currentRetryTimes = currentRetryTimes;
    }

    //getFilterConfigById
    public Rule.FilterConfig getFilterConfigById(String id){
        return rule.getFilterConfigById(id);
    }

    //getuniqueId
    public String getUniqueId(){
        return request.getTargetServiceUniqueId();
    }

    //releaseRequest
    public void releaseRequest(){
        if(requestReleased.compareAndSet(false, true)){
            ReferenceCountUtil.release(request.getFullHttpRequest());
        }
    }



//    @lombok.Builder
//    //builder
//    public static class Builder{
//        private String protocol;
//        private ChannelHandlerContext channelHandlerContext;
//        private boolean keepAlive;
//        private Rule rule;
//        private GatewayRequest request;
//
//        public Builder(){
//
//        }
//
//
//    }
}
