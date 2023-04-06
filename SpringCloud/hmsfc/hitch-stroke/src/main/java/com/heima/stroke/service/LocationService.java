package com.heima.stroke.service;

import com.heima.modules.po.LocationPO;

import java.util.List;

public interface LocationService {

    public void saveLocation(LocationPO locationPO);

    public LocationPO currentLocation(String tripid);

    /**
     * 批量添加数据
     * @param locationPOList
     */
    void batchSaveLocation(List<LocationPO> locationPOList);
}
