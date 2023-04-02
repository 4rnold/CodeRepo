package com.lagou.rabbitmq.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

public class ProducerApp {
    public static void main(String[] args) throws UnsupportedEncodingException {
        AbstractApplicationContext context
                = new ClassPathXmlApplicationContext("spring-rabbit.xml");

        RabbitTemplate template = context.getBean(RabbitTemplate.class);

        Message msg;

        final MessagePropertiesBuilder builder = MessagePropertiesBuilder.newInstance();
        builder.setContentEncoding("gbk");
        builder.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);

//        msg = MessageBuilder.withBody("你好，世界".getBytes("gbk"))
//                .andProperties(builder.build())
//                .build();
//
//        template.send("ex.direct", "routing.q1", msg);

        for (int i = 0; i < 1000; i++) {
            msg = MessageBuilder.withBody(("你好，世界" + i).getBytes("gbk"))
                    .andProperties(builder.build())
                    .build();

            template.send("ex.direct", "routing.q1", msg);
        }

        context.close();
    }
}
