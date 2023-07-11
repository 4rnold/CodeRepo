package mayfly.sys.biz.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.biz.controller.vo.ResourceListVO;
import mayfly.sys.biz.entity.ResourceDO;

import java.util.List;

/**
 * 资源服务接口
 *
 * @author hml
 * @date 2018/6/27 下午3:14
 */
public interface ResourceService extends BaseService<Long, ResourceDO> {

    /**
     * 根据用户id获取用户的所有菜单权限
     *
     * @param accountId 用户id
     * @return 菜单列表
     */
    List<ResourceListVO> listByAccountId(Long accountId);

    /**
     * 获取所有资源树
     *
     * @return 资源树
     */
    List<ResourceListVO> listResource();

    /**
     * 删除指定菜单，如果是有子节点，也删除
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * 创建资源
     *
     * @param resource 资源
     */
    void create(ResourceDO resource);

    /**
     * 更新资源
     *
     * @param resource 资源
     */
    void update(ResourceDO resource);

    /**
     * 改变菜单的权限
     *
     * @param id     id
     * @param status 状态
     */
    void changeStatus(Long id, Integer status);
}
