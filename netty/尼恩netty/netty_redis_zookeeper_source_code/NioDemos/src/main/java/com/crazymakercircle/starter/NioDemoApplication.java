package com.crazymakercircle.starter;

import com.crazymakercircle.iodemo.fileDemos.FileMmapDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration(
        exclude = {
                SecurityAutoConfiguration.class,
                //排除db的自动配置
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                //排除redis的自动配置
                RedisAutoConfiguration.class,
                RedisRepositoriesAutoConfiguration.class})
//自动加载配置信息
//@Configuration
//使包路径下带有@Value的注解自动注入
//使包路径下带有@Autowired的类可以自动注入
@ComponentScan("com.crazymakercircle.netty.http")
@SpringBootApplication
public class NioDemoApplication
{

    /**入口方法
     * @param args
     */
    public static void main(String[] args) {
        // 启动并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context =
                SpringApplication.run(NioDemoApplication.class, args);


        // java -jar NIODemos-1.0-SNAPSHOT.jar  mmaplock  demo.log true
        //  jvm选项    -Xms2G -Xmx8G
        //  命令 参数    mmaplock  10 1000000 cdh1
        //  第一个参数为 mmaplock， 启动 FileMmapDemo

        if (args.length > 0 && (args[0] != null && args[0].equalsIgnoreCase("mmaplock"))) {

            //启动 灵魂测试 客户端 1
            try {
                FileMmapDemo.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
