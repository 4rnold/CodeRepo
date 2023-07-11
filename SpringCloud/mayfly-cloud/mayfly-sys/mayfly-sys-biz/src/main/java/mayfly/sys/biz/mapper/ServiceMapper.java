package mayfly.sys.biz.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.biz.controller.query.ServiceQuery;
import mayfly.sys.biz.entity.ServiceDO;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:22
 */
public interface ServiceMapper extends BaseMapper<Long, ServiceDO> {

    List<ServiceDO> selectByQuery(ServiceQuery query);
}
