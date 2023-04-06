package com.heima.commons.entity;


import com.heima.commons.utils.PollingRound;

public class Round<T> {

    private PollingRound.TYPE type;

    private long delayTime = -1;

    private T result;

    public Round(PollingRound.TYPE type) {
        this.type = type;
    }

    public Round(PollingRound.TYPE type, T result) {
        this.type = type;
        this.result = result;
    }

    public Round(PollingRound.TYPE type, T result, long delayTime) {
        this.type = type;
        this.delayTime = delayTime;
        this.result = result;
    }


    public PollingRound.TYPE getType() {
        return type;
    }

    public void setType(PollingRound.TYPE type) {
        this.type = type;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}