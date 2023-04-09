package jiagoubaiduren.dao;

import jiagoubaiduren.mapper.store.StoreMapper;
import jiagoubaiduren.po.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StoreDao {

    @Autowired
    private StoreMapper storeMapper;

    public Store getOrderById(Long storeId) {
        return storeMapper.getOrderById(storeId);
    }
}
