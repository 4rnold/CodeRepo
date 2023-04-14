package com.itheima.service.impl;

import com.itheima.dao.log.LogInfoDao;
import com.itheima.dao.stock.StockDao;
import com.itheima.domain.LogInfo;
import com.itheima.domain.Stock;
import com.itheima.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private LogInfoDao logInfoDao;

    @Override
    public void save(Stock stock) {

        //测试用例1:此处发生异常(不用测)

        //保存stock到库存系统数据中的t_stock表
        int res1 = stockDao.save(stock);
        System.out.println("t_stock表受影响行数"+res1);

        //测试用例2:此处发生异常,stock已经保存到库存系统数据库库中t_stock表中,但是前面的保存要做要做回滚
//        System.out.println("发生异常:测试用例1...............................");
//        int i=1/0;
        //保存logInfo到日志数据库中t_log_info表
        LogInfo logInfo = new LogInfo();
        String id = UUID.randomUUID().toString().replace("-","").toUpperCase();
        logInfo.setId(id);
        logInfo.setCreateTime(new Date());
        logInfo.setMethod("save");
        logInfo.setAction("入库操作");
        logInfo.setUsername("test");
        int res2 = logInfoDao.save(logInfo);

        System.out.println("t_log_info表受影响行数"+res2);
        //测试用例3:此处发生异常,orderInfo已经保存到 order库中order_info表中,保存logInfo到log库中log_info表,但是前面的保存的orderinfo和loginfo要做要做回滚
        System.out.println("发生异常:测试用例2...............................");
        int i=1/0;

    }
}
