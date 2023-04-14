package com.itheima.service;

import com.itheima.domain.LogInfo;
import com.itheima.domain.Stock;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface StockService {

    /**
     * 入库
     * @param stock
     */
    void save(Stock stock);

}
