package com.heima.commons.domin.bo;

public class ZsetResultBO {
    public ZsetResultBO(Float score, String value) {
        this.score = score;
        this.value = value;
    }

    private Float score;
    private String value;

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
