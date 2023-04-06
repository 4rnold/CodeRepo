package com.heima.stroke.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    /**
     * 延迟时间 1分钟
     */
    private static final long DELAY_TIME = 1000 * 60;


    //行程超时队列
    public static final String STROKE_OVER_QUEUE = "STROKE_OVER_QUEUE";
    //行程死信队列
    public static final String STROKE_DEAD_QUEUE = "STROKE_DEAD_QUEUE";

    //行程位置队列
    public static final String STROKE_LOCATION_QUEUE = "STROKE_LOCATION_QUEUE";

    //行程超时队列交换器
    public static final String STROKE_OVER_QUEUE_EXCHANGE = "STROKE_OVER_QUEUE_EXCHANGE";

    //行程位置队列交换器
    public static final String STROKE_LOCATION_QUEUE_EXCHANGE = "STROKE_LOCATION_QUEUE_EXCHANGE";


    //行程死信队列交换器
    public static final String STROKE_DEAD_QUEUE_EXCHANGE = "STROKE_DEAD_QUEUE_EXCHANGE";
    //行程超时交换器 ROUTINGKEY
    public static final String STROKE_OVER_KEY = "STROKE_OVER_KEY";

    //行程死信交换器 ROUTINGKEY
    public static final String STROKE_DEAD_KEY = "STROKE_DEAD_KEY";

    //行程位置交换器 ROUTINGKEY
    public static final String STROKE_LOCATION_KEY = "STROKE_LOCATION_KEY";


    /**
     * 声明行程超时队列
     *
     * @return
     */
    @Bean
    public Queue strokeOverQueue() {
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", STROKE_DEAD_QUEUE_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", STROKE_DEAD_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", DELAY_TIME);
        return QueueBuilder.durable(STROKE_OVER_QUEUE).withArguments(args).build();
    }

    /**
     * 声明行程位置队列
     *
     * @return
     */
    @Bean
    public Queue strokeLocationQueue() {
        return QueueBuilder.durable(STROKE_LOCATION_QUEUE).build();
    }

    /**
     * 声明行程死信队列
     *
     * @return
     */
    @Bean
    public Queue strokeDeadQueue() {
        return new Queue(STROKE_DEAD_QUEUE, true);
    }

    /**
     * 创建行程超时队列交换器
     *
     * @return
     */
    @Bean
    DirectExchange strokeOverQueueExchange() {
        return new DirectExchange(STROKE_OVER_QUEUE_EXCHANGE, true, false);
    }

    /**
     * 创建行程死信队列交换器
     *
     * @return
     */
    @Bean
    DirectExchange strokeDeadQueueExchange() {
        return new DirectExchange(STROKE_DEAD_QUEUE_EXCHANGE, true, false);
    }

    /**
     * 创建行程位置队列交换器
     *
     * @return
     */
    @Bean
    DirectExchange strokeLocationQueueExchange() {
        return new DirectExchange(STROKE_LOCATION_QUEUE_EXCHANGE, true, false);
    }


    /**
     * 行程超时队列绑定
     *
     * @return
     */
    @Bean
    Binding bindingStrokeOverDirect() {
        return BindingBuilder.bind(strokeOverQueue()).
                to(strokeOverQueueExchange()).
                with(STROKE_OVER_KEY);
    }

    /**
     * 行程死信队列绑定
     *
     * @return
     */
    @Bean
    Binding bindingStrokeDeadDirect() {
        return BindingBuilder.bind(strokeDeadQueue()).
                to(strokeDeadQueueExchange()).
                with(STROKE_DEAD_KEY);
    }


    /**
     * 行程位置队列绑定
     *
     * @return
     */
    @Bean
    Binding bindingLocationOverDirect() {
        return BindingBuilder.bind(strokeLocationQueue()).
                to(strokeLocationQueueExchange()).
                with(STROKE_LOCATION_KEY);
    }


    @Bean(name = RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public RabbitListenerEndpointRegistry defaultRabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    /**
     * 设置批量消费
     * @param connectionFactory
     * @return
     */
    @Bean("batchQueueRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置批量
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);//设置BatchMessageListener生效
        factory.setBatchSize(10);//设置监听器一次批量处理的消息数量
        return factory;
    }
}
