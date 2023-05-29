package com.lagou.kafka.demo.listener;

import com.alibaba.fastjson.JSON;
import com.lagou.kafka.demo.entity.RetryRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@EnableScheduling
public class RetryListener {

    private Logger log = LoggerFactory.getLogger(RetryListener.class);

    private static final String RETRY_KEY_ZSET = "_retry_key";
    private static final String RETRY_VALUE_MAP = "_retry_value";
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topics.test}")
    private String bizTopic;

    @KafkaListener(topics = "${spring.kafka.topics.retry}")
//    public void consume(List<ConsumerRecord<String, String>> list) {
//        for(ConsumerRecord<String, String> record : list){
    public void consume(ConsumerRecord<String, String> record) {

        System.out.println("需要重试的消息：" + record);
        RetryRecord retryRecord = JSON.parseObject(record.value(), RetryRecord.class);

        /**
         * 防止待重试消息太多撑爆redis,可以将待重试消息按下一次重试时间分开存储放到不同介质
         * 例如下一次重试时间在半小时以后的消息储存到mysql,并定时从mysql读取即将重试的消息储储存到redis
         */

        // 通过redis的zset进行时间排序
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(RETRY_VALUE_MAP, key, record.value());
        redisTemplate.opsForZSet().add(RETRY_KEY_ZSET, key, retryRecord.getNextTime());
    }
//    }

    /**
     * 定时任务从redis读取到达重试时间的消息,发送到对应的topic
     */
//    @Scheduled(cron="2 * * * * *")
    @Scheduled(fixedDelay = 2000)
    public void retryFromRedis() {
        log.warn("retryFromRedis----begin");
        long currentTime = System.currentTimeMillis();
        // 根据时间倒序获取
        Set<ZSetOperations.TypedTuple<Object>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeByScoreWithScores(RETRY_KEY_ZSET, 0, currentTime);
        // 移除取出的消息
        redisTemplate.opsForZSet().removeRangeByScore(RETRY_KEY_ZSET, 0, currentTime);
        for(ZSetOperations.TypedTuple<Object> tuple : typedTuples){
            String key = tuple.getValue().toString();
            String value = redisTemplate.opsForHash().get(RETRY_VALUE_MAP, key).toString();
            redisTemplate.opsForHash().delete(RETRY_VALUE_MAP, key);
            RetryRecord retryRecord = JSON.parseObject(value, RetryRecord.class);
            ProducerRecord record = retryRecord.parse();

            ProducerRecord recordReal = new ProducerRecord(
                    bizTopic,
                    record.partition(),
                    record.timestamp(),
                    record.key(),
                    record.value(),
                    record.headers()
            );

            kafkaTemplate.send(recordReal);
        }
        // todo 发生异常将发送失败的消息重新发送到redis
    }
}