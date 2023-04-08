package com.itheima.controller;

import com.itheima.commons.enums.ResultEnum;
import com.itheima.commons.enums.TipsEnum;
import com.itheima.commons.pojo.CommonEntity;
import com.itheima.commons.result.ResponseData;
import com.itheima.service.ElasticsearchIndexService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Class: ElasticsearchIndexController
 * @Package com.itheima.controller
 * @Description:索引操作控制器
 * @Company: http://www.itheima.com/
 */
@RestController
@RequestMapping("v1/indices")
public class ElasticsearchIndexController {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchIndexController.class);
    @Autowired
    ElasticsearchIndexService elasticsearchIndexService;

    /*
     * @Description: 创建索引+映射
     * @Method: addIndexAndMapping
     * @Param: [commonEntity]
     * @Update:
     * @since: 1.0.0
     * @Return: com.itheima.commons.result.ResponseData
     *
     */
    @PostMapping(value = "/add")
    public ResponseData addIndexAndMapping(@RequestBody CommonEntity commonEntity) {
        //构造返回下游业务数据
        ResponseData rData = new ResponseData();
        if (StringUtils.isEmpty(commonEntity.getIndexName())) {
            rData.setResultEnum(ResultEnum.param_isnull);
            return rData;
        }
        //增加索引（映射）是否成功
        boolean isSuccess = false;
        try {
            //通过接口调用远程结构化查询方法
            isSuccess = elasticsearchIndexService.addIndexAndMapping(commonEntity);
            //通过类型推断自动装箱（多个参数取交集）
            rData.setResultEnum(isSuccess, ResultEnum.success, null);
            //日志记录
            logger.info(TipsEnum.create_index_success.getMessage());
        } catch (Exception e) {
            //打印到控制台
            e.printStackTrace();
            //日志记录
            logger.error(TipsEnum.create_index_fail.getMessage());
            //构建错误返回信息
            rData.setResultEnum(ResultEnum.error);
        }
        //返回
        return rData;

    }
}
