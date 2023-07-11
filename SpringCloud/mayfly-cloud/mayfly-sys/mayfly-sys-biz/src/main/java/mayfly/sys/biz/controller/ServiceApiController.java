package mayfly.sys.biz.controller;

import mayfly.core.log.LogContext;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.form.ServiceApiForm;
import mayfly.sys.biz.controller.query.ServiceApiQuery;
import mayfly.sys.biz.entity.ServiceApiDO;
import mayfly.sys.biz.service.ServiceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:47
 */
@RestController
@Response2Result
public class ServiceApiController {

    @Autowired
    private ServiceApiService serviceApiService;

    @Log(value = "获取服务api列表", resLevel = Log.Level.NONE)
    @GetMapping("/service/apis")
    public PageResult<ServiceApiDO> query(ServiceApiQuery query) {
        return PageResult.withPageHelper(query, () -> serviceApiService.listByQuery(query));
    }

    @Log("创建api")
    @PostMapping("/service/apis")
    public void create(@RequestBody @Valid ServiceApiForm form) {
        serviceApiService.create(BeanUtils.copy(form, ServiceApiDO.class));
    }

    @Log("修改api")
    @PutMapping("/service/apis/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid ServiceApiForm form) {
        LogContext.setNewObj(form);
        ServiceApiDO api = BeanUtils.copy(form, ServiceApiDO.class);
        api.setId(id);
        serviceApiService.update(api);
    }

    @Log("删除api")
    @DeleteMapping("/service/apis/{id}")
    public void delete(@PathVariable Long id) {
        serviceApiService.deleteById(id);
    }
}
