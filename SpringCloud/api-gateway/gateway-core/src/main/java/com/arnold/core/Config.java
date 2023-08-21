package com.arnold.core;


import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import lombok.Data;

/*
    网关的client config？请求服务的？
 */
@Data
public class Config {

    private int port = 8888;

    private String applicationName = "gateway";

    //注册中心地址
    private String registryAddress = "127.0.0.1:8848";

    //env
    private String env = "dev";

    private int eventLoopGroupBoosNum = 1;

    private int eventLoopGroupWorkerNum = Runtime.getRuntime().availableProcessors();

    //maxContentLength
    private int maxContentLength = 64 * 1024 * 1024;

    //whencomplete
    private boolean whenComplete = true;



    private int prometheusPort = 18000;


    // ----------------------------------------------	Http Async 参数选项：

    /**
     * 连接超时时间
     */
    private int httpConnectTimeout = 30 * 1000;

    /**
     * 请求超时时间
     */
    private int httpRequestTimeout = 30 * 1000;

    /**
     * 客户端请求重试次数
     */
    private int httpMaxRequestRetry = 2;

    /**
     * 客户端请求最大连接数
     */
    private int httpMaxConnections = 10000;

    /**
     * 客户端每个地址支持的最大连接数
     */
    private int httpConnectionsPerHost = 8000;

    /**
     * 客户端空闲连接超时时间, 默认60秒
     */
    private int httpPooledConnectionIdleTimeout = 60 * 1000;

    private String bufferType = "parallel";

    private int disruptor_bufferSize = 1024 * 16;

    private WaitStrategy disruptor_waitStrategy = new BlockingWaitStrategy();
    public Config() {
        System.out.println("config construct");
    }
}
