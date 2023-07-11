package mayfly.sys.biz.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.LogContext;
import mayfly.sys.biz.controller.query.ServiceQuery;
import mayfly.sys.biz.entity.ServiceDO;
import mayfly.sys.biz.mapper.ServiceMapper;
import mayfly.sys.biz.service.ServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:24
 */
@Service
public class ServiceServiceImpl extends BaseServiceImpl<ServiceMapper, Long, ServiceDO> implements ServiceService {

    @Override
    public List<ServiceDO> listByQuery(ServiceQuery query) {
        return mapper.selectByQuery(query);
    }

    @Override
    public void create(ServiceDO service) {
        BizAssert.equals(countByCondition(new ServiceDO().setCode(service.getCode())), 0L, "该服务code已存在");
        service.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(service);
    }

    @Override
    public void update(ServiceDO service) {
        ServiceDO oldService = getById(service.getId());
        LogContext.setOldObj(oldService);

        String newCode = service.getCode();
        // 如果修改了服务code，则需要校验code的唯一性
        if (!Objects.equals(newCode, oldService.getCode())) {
            BizAssert.equals(countByCondition(new ServiceDO().setCode(newCode)), 0L, "该服务code已存在");
        }
        updateByIdSelective(service);
    }
}
