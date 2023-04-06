package com.heima.stroke.service.impl;

import com.heima.commons.constant.HtichConstants;
import com.heima.modules.po.LocationPO;
import com.heima.stroke.rabbitmq.MQProducer;
import com.heima.stroke.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 添加位置信息
     *
     * @param locationPO
     */
    public void saveLocation(LocationPO locationPO) {
        mongoTemplate.insert(locationPO, HtichConstants.LOCATION_COLLECTION);
    }

    /**
     * 批量添加位置信息
     *
     * @param locationPOList
     */
    @Override
    public void batchSaveLocation(List<LocationPO> locationPOList) {
        mongoTemplate.insert(locationPOList, HtichConstants.LOCATION_COLLECTION);
    }

    @Override
    public LocationPO currentLocation(String tripid) {
        Criteria criteria = Criteria.where("trapId").is(tripid);
        Query query = new Query(criteria);
        query.limit(1);
        query.with(Sort.by(Sort.Order.desc("time")));
        return mongoTemplate.findOne(query, LocationPO.class, HtichConstants.LOCATION_COLLECTION);
    }


}
