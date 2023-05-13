package com.crazymakercircle.netty.http.echo;

import com.crazymakercircle.config.SystemConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HttpEchoServer
{
    // 抓包说明：由于WireShark只能抓取经过所监控的网卡的数据包
    //故，请求到localhost的本地包，默认是不能抓取到的。
    // 如果要抓取本地的调试包， 需要通过指令route指令增加服务器IP的路由表项配置
    // 只有这样，让发往本地服务器的报文，经过网卡所指向的网关
    // 从而，发往localhost(回环地址)的本地包就能被抓包工具从监控网卡抓取到
    // 具备办法，通过增加路由表项完成，其命令为route add，下面是一个例子
    //
    // route add 192.168.20.101 mask 255.255.255.255 192.168.20.1
    // route add 192.168.0.7 mask 255.255.255.255 192.168.0.1
    //
    // 以上命令表示：目标为192.168.20.101报文，其发送的下一跳为192.168.20.1网关
    // 该路由项在使用完毕后，建议删除，其删除指令如下：
    // route delete 192.168.20.101 mask 255.255.255.255 192.168.20.1 删除
    // 如果没有删除，则所有本机报文都经过网卡绕一圈回来，会很耗性能
    // 不过该路由表项并没有保存，在电脑重启后失效
    // 提示：以上的本地IP和网关IP，需要结合自己的电脑网卡和网关去更改
    // wireshark 报文过滤的条件  tcp.port == 18899

    static class ChildInitializer extends ChannelInitializer<SocketChannel>
    {


        @Override
        public void initChannel(SocketChannel ch)
        {
            ChannelPipeline pipeline = ch.pipeline();
            //请求解码器和响应编码器,等价于下面两行
            // pipeline.addLast(new HttpServerCodec());
            //请求解码器
            pipeline.addLast(new HttpRequestDecoder());
            //响应编码器
            pipeline.addLast(new HttpResponseEncoder());
            // HttpObjectAggregator 将HTTP消息的多个部分合成一条完整的HTTP消息
            // HttpObjectAggregator 用于解析Http完整请求
            // 把多个消息转换为一个单一的完全FullHttpRequest或是FullHttpResponse，
            // 原因是HTTP解码器会在解析每个HTTP消息中生成多个消息对象
            // 如 HttpRequest/HttpResponse,HttpContent,LastHttpContent
            pipeline.addLast(new HttpObjectAggregator(65535));
            // 自定义的业务handler
            pipeline.addLast(new HttpEchoHandler());
        }
    }

    /**
     * 启动服务，入口方法一
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        start();
    }
    /**
     * 启动服务
     */
    public static void start() throws InterruptedException
    {
        // 创建连接监听reactor 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建连接处理 reactor 线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            //服务端启动引导实例
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChildInitializer());

            // 监听端口，返回监听通道
            Channel ch = b.bind(SystemConfig.SOCKET_SERVER_PORT).sync().channel();

            log.info("HTTP ECHO 服务已经启动 http://{}:{}/"
                    , SystemConfig.SOCKET_SERVER_NAME
                    , SystemConfig.SOCKET_SERVER_PORT);
            // 等待服务端监听到端口关闭
            ch.closeFuture().sync();
        } finally
        {
            // 优雅地关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
