package mayfly.sys.biz.controller;

import mayfly.core.exception.BizAssert;
import mayfly.core.log.LogContext;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.form.ResourceForm;
import mayfly.sys.biz.controller.vo.ResourceDetailVO;
import mayfly.sys.biz.controller.vo.ResourceListVO;
import mayfly.sys.biz.entity.ResourceDO;
import mayfly.sys.api.enums.ResourceTypeEnum;
import mayfly.sys.biz.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 2:49 PM
 */
@Response2Result
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public List<ResourceListVO> list() {
        return resourceService.listResource();
    }

    @GetMapping("/{id}")
    public ResourceDetailVO detail(@PathVariable Long id) {
        return BeanUtils.copy(resourceService.getById(id), ResourceDetailVO.class);
    }

    @PostMapping()
    @Log("新增资源")
    public void add(@RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copy(resourceForm, ResourceDO.class);
        if (ResourceTypeEnum.MENU.getValue().equals(resourceForm.getType())) {
            BizAssert.notNull(resourceForm.getMeta(), "菜单元数据不能为空");
            resource.setMeta(JsonUtils.toJSONString(resourceForm.getMeta()));
        }
        resourceService.create(resource);
    }

    @Log("更新资源信息")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid ResourceForm resourceForm) {
        ResourceDO resource = BeanUtils.copy(resourceForm, ResourceDO.class);
        if (ResourceTypeEnum.MENU.getValue().equals(resourceForm.getType())) {
            BizAssert.notNull(resourceForm.getMeta(), "菜单元数据不能为空");
            resource.setMeta(JsonUtils.toJSONString(resourceForm.getMeta()));
        }
        LogContext.setNewObj(resourceForm);
        resource.setId(id);
        resourceService.update(resource);
    }

    @PutMapping("/{id}/{status}")
    @Log("修改资源状态")
    public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        resourceService.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @Log("删除资源")
    public void del(@PathVariable Long id) {
        resourceService.delete(id);
    }
}
