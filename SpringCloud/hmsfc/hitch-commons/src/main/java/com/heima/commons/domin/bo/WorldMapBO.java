package com.heima.commons.domin.bo;


public class WorldMapBO {

    public WorldMapBO() {

    }

    public WorldMapBO(GeoBO geoBO, String key) {
        this.location = new Location(geoBO.getLat(), geoBO.getLng());
        this.key = key;
        this.name = key;
    }

    private String key;
    private String name;
    private Location location;
    private int ratio;

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
