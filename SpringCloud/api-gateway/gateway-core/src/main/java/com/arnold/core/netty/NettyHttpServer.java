package com.arnold.core.netty;

import com.arnold.common.constants.GatewayConst;
import com.arnold.common.utils.RemotingUtil;
import com.arnold.core.Config;
import com.arnold.core.LifeCycle;
import com.arnold.core.netty.processor.DisruptorNettyCoreProcessor;
import com.arnold.core.netty.processor.NettyCoreProcessor;
import com.arnold.core.netty.processor.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Data
public class NettyHttpServer implements LifeCycle {

    private final Config config;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    private NettyProcessor nettyProcessor;

    public NettyHttpServer(Config config) {
        this.config = config;

        //按条件创建Processor
        NettyProcessor nettyCoreProcessor = new NettyCoreProcessor();
        if (GatewayConst.BUFFER_TYPE_PARALLEL.equals(config.getBufferType())) {
            nettyCoreProcessor = new DisruptorNettyCoreProcessor(config, nettyCoreProcessor);
        }

        this.nettyProcessor = nettyCoreProcessor;
        init();
    }


    @Override
    public void init() {
        if (useEpoll()) {
            this.serverBootstrap = new ServerBootstrap();
            this.bossEventLoopGroup = new EpollEventLoopGroup(config.getEventLoopGroupBoosNum(),
                    new DefaultThreadFactory("netty-boss-nio"));
            this.workerEventLoopGroup = new EpollEventLoopGroup(config.getEventLoopGroupWorkerNum(),
                    new DefaultThreadFactory("netty-worker-nio"));
        } else {
            this.serverBootstrap = new ServerBootstrap();
            this.bossEventLoopGroup = new NioEventLoopGroup(config.getEventLoopGroupBoosNum(),
                    new DefaultThreadFactory("netty-boss-nio"));
            this.workerEventLoopGroup = new NioEventLoopGroup(config.getEventLoopGroupWorkerNum(),
                    new DefaultThreadFactory("netty-worker-nio"));
        }

    }

    public boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform() && Epoll.isAvailable();
    }

    @Override
    public void start() {
        this.serverBootstrap
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(NioChannelOption.SO_KEEPALIVE, true)
                .localAddress(new InetSocketAddress(config.getPort()))//和bind()没区别
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(config.getMaxContentLength()),
                                new NettyServerConnectManagerHandler(),//这有啥用
                                new NettyHttpServerHandler(nettyProcessor)

                        );

                    }
                });

        try {
            this.serverBootstrap.bind().sync();
            log.info("server startup on port:{}", config.getPort());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // processor start
        nettyProcessor.start();
    }

    @Override
    public void shutdown() {
        if (bossEventLoopGroup != null) {
            bossEventLoopGroup.shutdownGracefully();
        }
        if (workerEventLoopGroup != null) {
            workerEventLoopGroup.shutdownGracefully();
        }
    }
}
