package com.lagou.rocket.demo.consumer;

import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class MyConsumer {

    // 消费组名称
    private static final String GROUP_NAME = "consumer_grp_13_01";
    // 主题名称
    private static final String TOPIC_NAME = "tp_demo_13";
    // consumer_grp_13_01:tp_demo_13
    private static final String KEY = String.format("%s:%s", GROUP_NAME, TOPIC_NAME);
    // 使用map存放主题每个MQ的偏移量
    private static final Map<MessageQueue, Long> OFFSET_TABLE = new HashMap<MessageQueue, Long>();

    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    // 具有固定大小的线程池
    private static final ExecutorService pool = Executors.newFixedThreadPool(32);

    private static final AtomicLong SUCCESS_COUNT = new AtomicLong(0);
    private static final AtomicLong FAIL_COUNT = new AtomicLong(0);

    public static void main(String[] args) throws MQClientException {
        // 初始化哨兵的流控
        initFlowControlRule();

        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(GROUP_NAME);
        consumer.setNamesrvAddr("node1:9876");
        consumer.start();

        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(TOPIC_NAME);
        for (MessageQueue mq : mqs) {
            System.out.printf("Consuming messages from the queue: %s%n", mq);

            SINGLE_MQ:
            while (true) {
                try {
                    PullResult pullResult =
                            consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    if (pullResult.getMsgFoundList() != null) {
                        for (MessageExt msg : pullResult.getMsgFoundList()) {
                            doSomething(msg);
                        }
                    }

                    long nextOffset = pullResult.getNextBeginOffset();
                    // 将每个mq对应的偏移量记录在本地HashMap中
                    putMessageQueueOffset(mq, nextOffset);
                    consumer.updateConsumeOffset(mq, nextOffset);
                    switch (pullResult.getPullStatus()) {
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case FOUND:
                        case NO_MATCHED_MSG:
                        case OFFSET_ILLEGAL:
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        consumer.shutdown();
    }

    /**
     * 对每个收到的消息使用一个线程提交任务
     * @param message
     */
    private static void doSomething(MessageExt message) {
        pool.submit(() -> {
            Entry entry = null;
            try {
                // 应用流控规则
                ContextUtil.enter(KEY);
                entry = SphU.entry(KEY, EntryType.OUT);

                // 在这里处理业务逻辑，此处只是打印
                System.out.printf("[%d][%s][Success: %d] Receive New Messages: %s %n", System.currentTimeMillis(),
                        Thread.currentThread().getName(), SUCCESS_COUNT.addAndGet(1), new String(message.getBody()));
            } catch (BlockException ex) {
                // Blocked.
                System.out.println("Blocked: " + FAIL_COUNT.addAndGet(1));
            } finally {
                if (entry != null) {
                    entry.exit();
                }
                ContextUtil.exit();
            }
        });
    }

    private static void initFlowControlRule() {
        FlowRule rule = new FlowRule();
        // 消费组名称:主题名称   字符串
        rule.setResource(KEY);
        // 根据QPS进行流控
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 1表示QPS为1，请求间隔1000ms。
        // 如果是5，则表示每秒5个消息，请求间隔200ms
        rule.setCount(1);
        rule.setLimitApp("default");

        // 调用使用固定间隔。如果qps为1，则请求之间间隔为1s
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        // 如果请求太多，就将这些请求放到等待队列中
        // 该队列有超时时间。如果等待队列中请求超时，则丢弃
        // 此处设置超时时间为5s
        rule.setMaxQueueingTimeMs(5 * 1000);
        // 使用流控管理器加载流控规则
        FlowRuleManager.loadRules(Collections.singletonList(rule));
    }

    // 获取指定MQ的偏移量
    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSET_TABLE.get(mq);
        if (offset != null) {
            return offset;
        }

        return 0;
    }

    // 在本地HashMap中记录偏移量
    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSET_TABLE.put(mq, offset);
    }
}