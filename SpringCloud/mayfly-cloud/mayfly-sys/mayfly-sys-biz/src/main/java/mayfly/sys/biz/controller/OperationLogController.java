package mayfly.sys.biz.controller;

import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.query.OperationLogQuery;
import mayfly.sys.biz.entity.OperationLogDO;
import mayfly.sys.biz.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @date 2020-03-05 4:05 下午
 */
@Response2Result
@RestController
@RequestMapping("/logs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public PageResult<OperationLogDO> list(OperationLogQuery query) {
        return operationLogService.listByCondition(BeanUtils.copy(query, OperationLogDO.class), query);
    }
}
