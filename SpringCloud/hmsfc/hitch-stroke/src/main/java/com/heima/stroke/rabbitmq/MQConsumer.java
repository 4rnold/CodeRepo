package com.heima.stroke.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.heima.commons.utils.CommonsUtils;
import com.heima.modules.po.LocationPO;
import com.heima.modules.vo.LocationVO;
import com.heima.modules.vo.StrokeVO;
import com.heima.stroke.configuration.RabbitConfig;
import com.heima.stroke.handler.StrokeHandler;
import com.heima.stroke.service.LocationService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 行程消费者类
 */
@Component
public class MQConsumer implements BatchMessageListener {


    @Autowired
    private StrokeHandler strokeHandler;

    @Autowired
    private LocationService locationService;

    /**
     * 行程超时监听
     *
     * @param massage
     * @param channel
     * @param tag
     */
    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.STROKE_DEAD_QUEUE, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.STROKE_DEAD_QUEUE_EXCHANGE), key = RabbitConfig.STROKE_DEAD_KEY)
                    })
    @RabbitHandler
    public void processStroke(Message massage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        StrokeVO strokeVO = JSON.parseObject(massage.getBody(), StrokeVO.class);
        if (null == strokeVO) {
            return;
        }
        try {
            strokeHandler.timeoutHandel(strokeVO);
            //手动确认机制
            channel.basicAck(tag, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 行程位置监听
     *
     */

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = RabbitConfig.STROKE_LOCATION_QUEUE, durable = "true"),
                                    exchange = @Exchange(value = RabbitConfig.STROKE_LOCATION_QUEUE_EXCHANGE), key = RabbitConfig.STROKE_LOCATION_KEY)
                    }, concurrency = "10")
    @Override
    public void onMessageBatch(List<Message> messages) {
        List<LocationPO> locationPOList = getLocationPOList(messages);
        locationService.batchSaveLocation(locationPOList);
    }

    private List<LocationPO> getLocationPOList(List<Message> messages) {
        List<LocationPO> locationPOList = new ArrayList<>();
        for (Message message : messages) {
            LocationVO locationVO = JSON.parseObject(message.getBody(), LocationVO.class);
            locationPOList.add(CommonsUtils.toPO(locationVO));
        }
        return locationPOList;
    }
}
