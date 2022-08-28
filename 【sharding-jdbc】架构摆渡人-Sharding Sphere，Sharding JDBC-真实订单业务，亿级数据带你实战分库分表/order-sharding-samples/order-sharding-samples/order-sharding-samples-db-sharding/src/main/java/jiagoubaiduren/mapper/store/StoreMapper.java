package jiagoubaiduren.mapper.store;

import jiagoubaiduren.po.Store;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {

    Store getOrderById(Long storeId);

}
