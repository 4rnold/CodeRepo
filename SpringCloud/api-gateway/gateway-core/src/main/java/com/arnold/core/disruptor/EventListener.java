package com.arnold.core.disruptor;

public interface EventListener<E> {
    void onEvent(E event);

    void onException(Throwable throwable,long sequence,E event);
}
