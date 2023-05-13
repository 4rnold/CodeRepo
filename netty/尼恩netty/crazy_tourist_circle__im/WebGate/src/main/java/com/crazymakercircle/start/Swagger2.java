package com.crazymakercircle.start;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/


import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2
{
    //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi()
    {
        //为当前包路径,这个包指的是在哪些类中使用swagger2来测试
        Predicate<RequestHandler> selector = RequestHandlerSelectors
                .basePackage("com.crazymakercircle.controller");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(selector)
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                //创建人
                .contact(new Contact("尼恩@疯狂创客圈", "https://www.cnblogs.com/crazymakercircle/", ""))
                //版本号
                .version("1.0")
                .termsOfServiceUrl("http://localhost:8080/swagger-ui.html")   //描述
                .description("API 描述")
                .build();
    }


}
