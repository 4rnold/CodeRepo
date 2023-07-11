package mayfly.sys.biz.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.biz.controller.query.ServiceApiQuery;
import mayfly.sys.biz.entity.ServiceApiDO;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:25
 */
public interface ServiceApiService extends BaseService<Long, ServiceApiDO> {

    List<ServiceApiDO> listByQuery(ServiceApiQuery query);

    /**
     * 创建api信息
     *
     * @param apiDO api
     */
    void create(ServiceApiDO apiDO);

    /**
     * 修改api信息
     *
     * @param api api
     */
    void update(ServiceApiDO api);
}
