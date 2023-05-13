package com.crazymakercircle.imServer.starter;

import com.crazymakercircle.imServer.handler.MultiEchoHandler;
import com.crazymakercircle.imServer.handler.SoulTest1ServerHandler;
import com.crazymakercircle.imServer.server.ChatServer;
import com.crazymakercircle.imServer.server.EchoIOUringServer;
import com.crazymakercircle.imServer.server.MultiEchoServer;
import com.crazymakercircle.imServer.server.SoulTest1Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


//自动加载配置信息
@Configuration
//使包路径下带有@Value的注解自动注入
//使包路径下带有@Autowired的类可以自动注入
@ComponentScan("com.crazymakercircle.imServer")
@SpringBootApplication
@Slf4j
public class ServerApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 启动并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context =
                SpringApplication.run(ServerApplication.class, args);


        if (args.length > 0 &&
                (args[0] != null && args[0].equalsIgnoreCase("soultest1"))) {
            //启动 灵魂测试1  服务器

            //  jvm选项    -Xms2G -Xmx8G
            //  命令 参数   soultest1 10 1000000 cdh1
            //  第一个参数为 soultest1， 启动百万连接 netty 灵魂测试客户端

            startSoultest1Server(context, args);

        } else if (args.length > 0 &&
                (args[0] != null && args[0].equalsIgnoreCase("multiEcho"))) {

            //  jvm选项    -Xms2G -Xmx8G
            //  命令 参数   multiEcho  2
            // 第二个参数为重复回复的次数


            //启动重复回显的服务器
            startMultiEchoServer(context, args);
        } else if (args.length > 0 &&
                (args[0] != null && args[0].equalsIgnoreCase("ioUring"))) {

            //  jvm选项    -Xms2G -Xmx8G
            //  命令 参数   ioUring
            // 第二个参数为重复回复的次数


            //启动重复回显的服务器
            startIoUringServer(context);
        } else {
            //启动聊天服务器
            startChatServer(context);
        }
    }

    //启动 重复回显的服务器
    private static void startMultiEchoServer(ApplicationContext context, String[] args) {

        MultiEchoServer multiEchoServer = context.getBean(MultiEchoServer.class);
        MultiEchoHandler multiEchoHandler = context.getBean(MultiEchoHandler.class);
        if (args.length > 1 && args[1] != null) {
            //重复回显的次数
            multiEchoHandler.repeat_time = Integer.parseInt(args[1]);
        }
        multiEchoServer.run();

    }

    //启动 灵魂测试 服务端 1
    private static void startSoultest1Server(ApplicationContext context, String[] args) {

        SoulTest1Server soulTest1Server = context.getBean(SoulTest1Server.class);
        SoulTest1ServerHandler soulTest1ServerHandler = context.getBean(SoulTest1ServerHandler.class);

        if (args.length > 1 && args[1] != null) {
            //设置的端口数，如100
            SoulTest1Server.serverPortCount = Long.parseLong(args[1]);
            log.debug("设置的端口数，{}", SoulTest1Server.serverPortCount);

            soulTest1ServerHandler.init();
        }
        soulTest1Server.run();

    }

    //启动聊天服务器
    private static void startChatServer(ApplicationContext context) {
        ChatServer nettyServer = context.getBean(ChatServer.class);
        nettyServer.run();
    }

    //启动IoUring 服务器
    private static void startIoUringServer(ApplicationContext context) {
        EchoIOUringServer server = context.getBean(EchoIOUringServer.class);
        server.run();
    }


}

