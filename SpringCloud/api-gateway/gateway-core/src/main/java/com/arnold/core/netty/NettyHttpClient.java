package com.arnold.core.netty;

import com.arnold.core.Config;
import com.arnold.core.LifeCycle;
import com.arnold.core.helper.AsyncHttpHelper;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;

/**
 * 封装AsyncHttpClient
 */
public class NettyHttpClient implements LifeCycle {

    private final Config config;

    private final EventLoopGroup workerEventLoopGroup;

    private AsyncHttpClient asyncHttpClient;


    public NettyHttpClient(Config config, EventLoopGroup workerEventLoopGroup) {
        this.config = config;
        this.workerEventLoopGroup = workerEventLoopGroup;
        init();
    }

    @Override
    public void init() {
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(workerEventLoopGroup)
                // 超时时间
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                // 重试次数
                .setMaxRedirects(config.getHttpMaxRequestRetry())
                // 池化 ByteBuffer 分配器 提升性能
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpConnectionsPerHost())
                .setMaxRequestRetry(0)
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());

        // 初始化 asyncHttpClient
        this.asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
    }

    @Override
    public void start() {
        AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
    }

    @Override
    public void shutdown() {
        if (asyncHttpClient != null) {
            try {
                asyncHttpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
