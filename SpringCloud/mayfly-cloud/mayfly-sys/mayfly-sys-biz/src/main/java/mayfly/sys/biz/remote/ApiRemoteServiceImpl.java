package mayfly.sys.biz.remote;

import mayfly.core.model.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.api.ApiRemoteService;
import mayfly.sys.api.model.query.ApiQueryDTO;
import mayfly.sys.api.model.query.ServiceQueryDTO;
import mayfly.sys.api.model.res.ApiDTO;
import mayfly.sys.api.model.res.ServiceDTO;
import mayfly.sys.biz.controller.query.ServiceApiQuery;
import mayfly.sys.biz.controller.query.ServiceQuery;
import mayfly.sys.biz.service.ServiceApiService;
import mayfly.sys.biz.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-30 17:22
 */
@RestController
public class ApiRemoteServiceImpl implements ApiRemoteService {

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServiceApiService serviceApiService;

    @Override
    public Result<List<ServiceDTO>> listServiceByQuery(ServiceQueryDTO query) {
        ServiceQuery q = BeanUtils.copy(query, ServiceQuery.class);
        return Result.success(BeanUtils.copy(serviceService.listByQuery(q), ServiceDTO.class));
    }

    @Override
    public Result<List<ApiDTO>> listApiByQuery(ApiQueryDTO query) {
        ServiceApiQuery q = BeanUtils.copy(query, ServiceApiQuery.class);
        return Result.success(BeanUtils.copy(serviceApiService.listByQuery(q), ApiDTO.class));
    }
}
