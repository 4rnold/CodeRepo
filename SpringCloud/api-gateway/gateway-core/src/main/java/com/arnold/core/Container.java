package com.arnold.core;

import com.arnold.core.netty.NettyHttpClient;
import com.arnold.core.netty.NettyHttpServer;
import com.arnold.core.netty.processor.NettyCoreProcessor;
import com.arnold.core.netty.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Container implements LifeCycle{

    private final Config config;

    //这个client是gateway向Server发起请求的client，NettyServer的EventLoop
    //内部使用AsyncHttpClient
    private NettyHttpClient nettyHttpClient;

    private NettyHttpServer nettyHttpServer;

//    private NettyProcessor nettyProcessor;

    public Container(Config config) {
        this.config = config;
        init();
    }

    @Override
    public void init() {

        this.nettyHttpServer = new NettyHttpServer(config);
        //这个client是gateway向Server发起请求的client，NettyServer的EventLoop
        this.nettyHttpClient = new NettyHttpClient(config, nettyHttpServer.getWorkerEventLoopGroup());
    }

    @Override
    public void start() {
        nettyHttpServer.start();
        nettyHttpClient.start();
        log.info("gateway start");

    }

    @Override
    public void shutdown() {

    }
}
