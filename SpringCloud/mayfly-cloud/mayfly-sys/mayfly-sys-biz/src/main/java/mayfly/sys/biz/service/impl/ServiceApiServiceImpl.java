package mayfly.sys.biz.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.enums.BooleanEnum;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.LogContext;
import mayfly.sys.biz.controller.query.ServiceApiQuery;
import mayfly.sys.biz.entity.ServiceApiDO;
import mayfly.sys.biz.entity.ServiceDO;
import mayfly.sys.biz.mapper.ServiceApiMapper;
import mayfly.sys.biz.service.ServiceApiService;
import mayfly.sys.biz.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:26
 */
@Service
public class ServiceApiServiceImpl extends BaseServiceImpl<ServiceApiMapper, Long, ServiceApiDO> implements ServiceApiService {

    @Autowired
    private ServiceService serviceService;

    @Override
    public List<ServiceApiDO> listByQuery(ServiceApiQuery query) {
        return mapper.selectByQuery(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(ServiceApiDO api) {
        ServiceDO service = serviceService.getById(api.getServiceId());
        BizAssert.notNull(service, "服务不存在");
        api.setServiceCode(service.getCode());

        BizAssert.equals(countByCondition(new ServiceApiDO().setMethod(api.getMethod()).setUri(api.getUri())), 0L, "该请求地址已存在");
        api.setStatus(EnableDisableEnum.ENABLE.getValue());
        api.setIsDeleted(BooleanEnum.FALSE.getValue());
        insert(api);
        updateService(api.getServiceId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ServiceApiDO api) {
        ServiceApiDO oldApi = getById(api.getId());
        BizAssert.notNull(oldApi, "api信息不存在");
        LogContext.setOldObj(oldApi);

        updateByIdSelective(api);
        updateService(api.getServiceId());
    }

    private void updateService(Long serviceId) {
        ServiceDO service = new ServiceDO();
        service.setId(serviceId);
        serviceService.updateByIdSelective(service);
    }
}
