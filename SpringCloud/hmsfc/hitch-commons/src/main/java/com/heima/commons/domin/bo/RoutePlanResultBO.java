package com.heima.commons.domin.bo;

public class RoutePlanResultBO {

    /**
     * 距离数据
     */
    private TextValue distance;
    /**
     * 耗时
     */
    private TextValue duration;

    public TextValue getDistance() {
        return distance;
    }

    public void setDistance(TextValue distance) {
        this.distance = distance;
    }

    public TextValue getDuration() {
        return duration;
    }

    public void setDuration(TextValue duration) {
        this.duration = duration;
    }
}
