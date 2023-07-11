package mayfly.sys.biz.controller;

import mayfly.core.log.LogContext;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.form.ServiceForm;
import mayfly.sys.biz.controller.query.ServiceQuery;
import mayfly.sys.biz.entity.ServiceDO;
import mayfly.sys.biz.service.ServiceService;
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
 * @date 2022-03-28 21:33
 */
@RestController
@Response2Result
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Log(value = "获取服务列表信息", resLevel = Log.Level.NONE)
    @GetMapping("/services")
    public PageResult<ServiceDO> query(ServiceQuery query) {
        return PageResult.withPageHelper(query, () -> serviceService.listByQuery(query));
    }

    @Log("创建服务")
    @PostMapping("/services")
    public void create(@RequestBody @Valid ServiceForm serviceForm) {
        serviceService.create(BeanUtils.copy(serviceForm, ServiceDO.class));
    }

    @Log("修改服务")
    @PutMapping("/services/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid ServiceForm serviceForm) {
        LogContext.setNewObj(serviceForm);
        ServiceDO service = BeanUtils.copy(serviceForm, ServiceDO.class);
        service.setId(id);
        serviceService.update(service);
    }

    @Log("删除服务")
    @DeleteMapping("/services/{id}")
    public void delete(@PathVariable Long id) {
        serviceService.fakeDeleteById(id);
    }
}
