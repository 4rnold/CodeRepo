package com.tangyh.lamp.activiti.service.biz;

import com.tangyh.basic.base.service.SuperService;
import com.tangyh.lamp.activiti.dto.biz.BizItemResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizItem;

import java.util.List;

/**
 * <p>
 * 业务接口
 * <p>
 * </p>
 *
 * @author wz
 * @date 2020-08-19
 */
public interface BizItemService extends SuperService<BizItem> {

    /**
     * 保存实体
     *
     * @param po 实体
     * @return 是否成功
     */
    boolean saveItem(BizItem po);

    /**
     * 查询实体
     *
     * @param instId 实例id
     * @return 业务节点
     */
    List<BizItemResDTO> find(String instId);
}
