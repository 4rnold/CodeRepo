package com.heima.commons.domin.bo;

import com.heima.commons.utils.CommonsUtils;

import java.io.Serializable;
import java.util.Objects;

public class GeoBO implements Serializable {
    public GeoBO(String targetId, float distance, String lng, String lat) {
        this.targetId = targetId;
        this.distance = distance;
        this.lng = lng;
        this.lat = lat;
    }

    public GeoBO() {

    }

    private String targetId;
    private float distance;
    private String lng;
    private String lat;


    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "GeoBO{" +
                "targetId='" + targetId + '\'' +
                '}';
    }

    public Float toKilometre() {
        return Float.parseFloat(CommonsUtils.floatToStr(distance));
    }

    public static void main(String[] args) {
        System.out.println(Float.parseFloat(CommonsUtils.floatToStr(0.1f)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoBO geoBO = (GeoBO) o;
        return targetId.equals(geoBO.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId);
    }
}
