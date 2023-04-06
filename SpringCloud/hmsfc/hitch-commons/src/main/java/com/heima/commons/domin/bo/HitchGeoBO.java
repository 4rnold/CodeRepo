package com.heima.commons.domin.bo;

public class HitchGeoBO {

    public HitchGeoBO(String targetId, GeoBO startGeo, GeoBO endGeo) {
        this.targetId = targetId;
        this.startGeo = startGeo;
        this.endGeo = endGeo;
    }


    public HitchGeoBO(String targetId) {
        this.targetId = targetId;
    }

    private String targetId;
    private GeoBO startGeo;
    private GeoBO endGeo;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public GeoBO getStartGeo() {
        return startGeo;
    }

    public void setStartGeo(GeoBO startGeo) {
        this.startGeo = startGeo;
    }

    public GeoBO getEndGeo() {
        return endGeo;
    }

    public void setEndGeo(GeoBO endGeo) {
        this.endGeo = endGeo;
    }
}
