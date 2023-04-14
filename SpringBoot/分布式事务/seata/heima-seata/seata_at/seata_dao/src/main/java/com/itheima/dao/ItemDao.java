package com.itheima.dao;

import com.itheima.domain.Item;
import org.apache.ibatis.annotations.Update;

/***
 *
 *
 ****/
public interface ItemDao {

    /***
     * 修改库存数量
     * @param item
     * @return
     */
    @Update("update item set num=num-#{num} where id=#{id}")
    int update(Item item);
}
