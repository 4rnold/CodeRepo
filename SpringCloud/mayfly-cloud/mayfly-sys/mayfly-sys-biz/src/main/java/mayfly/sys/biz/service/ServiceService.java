package mayfly.sys.biz.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.biz.controller.query.ServiceQuery;
import mayfly.sys.biz.entity.ServiceDO;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:24
 */
public interface ServiceService extends BaseService<Long, ServiceDO> {

    List<ServiceDO> listByQuery(ServiceQuery query);

    /**
     * 创建服务
     *
     * @param service 服务信息
     */
    void create(ServiceDO service);

    /**
     * 更新服务
     *
     * @param service 服务信息
     */
    void update(ServiceDO service);
}
