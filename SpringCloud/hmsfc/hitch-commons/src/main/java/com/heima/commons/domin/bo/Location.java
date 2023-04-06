package com.heima.commons.domin.bo;

public class Location {

    public Location(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    private String lat;
    private String lon;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
