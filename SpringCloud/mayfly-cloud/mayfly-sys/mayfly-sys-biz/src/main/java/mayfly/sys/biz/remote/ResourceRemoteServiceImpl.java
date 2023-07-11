package mayfly.sys.biz.remote;

import mayfly.core.model.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.api.ResourceRemoteService;
import mayfly.sys.api.model.res.ResourceDTO;
import mayfly.sys.biz.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-20 21:23
 */
@RestController
public class ResourceRemoteServiceImpl implements ResourceRemoteService {

    @Autowired
    private ResourceService resourceService;

    @Override
    public Result<List<ResourceDTO>> listByAccountId(Long accountId) {
        return Result.success(BeanUtils.copy(resourceService.listByAccountId(accountId), ResourceDTO.class));
    }
}
