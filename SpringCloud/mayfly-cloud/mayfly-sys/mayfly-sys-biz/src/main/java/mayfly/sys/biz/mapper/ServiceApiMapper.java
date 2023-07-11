package mayfly.sys.biz.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.biz.controller.query.ServiceApiQuery;
import mayfly.sys.biz.entity.ServiceApiDO;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:23
 */
public interface ServiceApiMapper extends BaseMapper<Long, ServiceApiDO> {

    List<ServiceApiDO> selectByQuery(ServiceApiQuery query);
}
