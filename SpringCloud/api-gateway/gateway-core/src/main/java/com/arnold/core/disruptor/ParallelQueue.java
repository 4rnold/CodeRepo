package com.arnold.core.disruptor;

public interface ParallelQueue<E> {

    void add(E event);

    void add(E... events);

    void start();

    void shutDown();


}
