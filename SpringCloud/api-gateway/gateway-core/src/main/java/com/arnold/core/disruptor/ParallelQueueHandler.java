package com.arnold.core.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.sun.java.accessibility.util.Translator;
import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelQueueHandler<E> implements ParallelQueue<E> {

    private ExecutorService executorService;

    //真正的处理器,包括正常处理和异常处理
    private EventListener<E> eventListener;

    //将E放入disruptor中的坑(Holder对象)中
    private EventTranslatorOneArg<EventHolder, E> eventTranslator;

    private RingBuffer<EventHolder> ringBuffer;

    private WorkerPool<EventHolder> workerPool;


    public ParallelQueueHandler(int numThreads,
                                String threadNamePrefix,
                                EventListener<E> eventListener,
                                ProducerType producerType,
                                int bufferSize,
                                WaitStrategy waitStrategy) {
        this.executorService = Executors.newFixedThreadPool(numThreads,
                new ThreadFactoryBuilder().setNameFormat("ParallelQueueHandler" + threadNamePrefix + "-pool-%d").build());
        this.eventTranslator = new EventHolderTranslator();
        this.eventListener = eventListener;

        ringBuffer = RingBuffer.create(producerType, new EventHolderFactory(), bufferSize, waitStrategy);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建消费者
        WorkHandler[] workHandlers = new WorkHandler[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workHandlers[i] = new EventHolderWorkHandler();
        }
        WorkerPool<EventHolder> eventHolderWorkerPool = new WorkerPool<EventHolder>(ringBuffer, sequenceBarrier, new EventHolderExceptionHandler(), workHandlers);
        ringBuffer.addGatingSequences(eventHolderWorkerPool.getWorkerSequences());
        workerPool = eventHolderWorkerPool;
    }



    public ParallelQueueHandler() {
    }

    @Override
    public void add(E event) {
//todo
        try {
            ringBuffer.publishEvent(eventTranslator, event);
        } catch (Exception e) {
            eventListener.onException(e, -1, event);
        }
    }

    @Override
    public void add(E... events) {

    }

    @Override
    public void start() {
        this.ringBuffer = workerPool.start(executorService);
    }

    @Override
    public void shutDown() {
        ringBuffer = null;
        if (executorService != null) {
            executorService.shutdown();
        }
        if (workerPool != null) {
            workerPool.drainAndHalt();
        }
    }

    @Data
    public class RequestEvent {
        private E event;
    }


    //disruptor占位槽
    @Data
    public class EventHolder {
        private E event;
    }

    //对象放入槽
    private class EventHolderTranslator implements EventTranslatorOneArg<EventHolder, E> {

        @Override
        public void translateTo(EventHolder eventHolder, long l, E e) {
            eventHolder.setEvent(e);
        }
    }

    //槽填充
    private class EventHolderFactory implements EventFactory<EventHolder> {
        @Override
        public EventHolder newInstance() {
            return new EventHolder();
        }
    }

    //consumer
    public class EventHolderWorkHandler implements WorkHandler<EventHolder> {

        @Override
        public void onEvent(EventHolder eventHolder) throws Exception {
            eventListener.onEvent(eventHolder.getEvent());
            eventHolder.setEvent(null);

        }
    }

    public class EventHolderExceptionHandler implements ExceptionHandler<EventHolder> {
        @Override
        public void handleEventException(Throwable throwable, long l, EventHolder eventHolder) {
            eventListener.onException(throwable, l, eventHolder.getEvent());
            eventHolder.setEvent(null);
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            throw new UnsupportedOperationException(throwable);

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            throw new UnsupportedOperationException(throwable);

        }
    }


}
