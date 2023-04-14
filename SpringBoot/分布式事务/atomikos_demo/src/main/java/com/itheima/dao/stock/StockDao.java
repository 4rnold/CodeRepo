package com.itheima.dao.stock;

import com.itheima.domain.Stock;
import org.apache.ibatis.annotations.Insert;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface StockDao {

    /**
     * 添加库存
     * @param stock
     */
    @Insert("insert into t_stock(id,product_id,product_name,stock_id,quantity)values(#{id},#{productId},#{productName},#{stockId},#{quantity})")
    int save(Stock stock);
}
