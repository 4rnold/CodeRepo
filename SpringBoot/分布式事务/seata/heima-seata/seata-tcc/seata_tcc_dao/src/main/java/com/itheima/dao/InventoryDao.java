package com.itheima.dao;

import com.itheima.domain.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface InventoryDao {


    /**
     * 预扣减库存
     * @param productId
     * @param count
     * @return
     */
    @Update("update inventory set total_inventory = total_inventory - #{count}," +
            " lock_inventory= lock_inventory + #{count} " +
            " where product_id =#{productId}  and  total_inventory >0  ")
    int decrease(@Param("productId") String productId,@Param("count") Integer count);


    /**
     * 成功方法，把锁定库存清零
     * @param productId
     * @param count
     * @return
     */
    @Update("update inventory set " +
            " lock_inventory=  lock_inventory - #{count} " +
            " where product_id =#{productId}  and lock_inventory >0 ")
    int confirm(@Param("productId") String productId,@Param("count") Integer count);


    /**
     * 失败方法，把锁定库存加回到可用库存
     * @param productId
     * @param count
     * @return
     */
    @Update("update inventory set total_inventory = total_inventory + #{count}," +
            " lock_inventory= lock_inventory - #{count} " +
            " where product_id =#{productId}  and lock_inventory >0 ")
    int cancel(@Param("productId") String productId,@Param("count") Integer count);

    /**
     * Find by product id inventory do.
     *
     * @param productId the product id
     * @return the inventory do
     */
    @Select("select id,product_id,total_inventory ,lock_inventory from inventory where product_id =#{productId}")
    Inventory findByProductId(String productId);
}
