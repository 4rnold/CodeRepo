package mayfly.sys.biz.service;

import mayfly.core.permission.registry.LoginAccountRegistry;
import mayfly.sys.biz.controller.vo.LoginSuccessVO;
import mayfly.sys.biz.entity.AccountDO;

/**
 * 权限服务
 *
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService extends LoginAccountRegistry {
    /**
     * 保存id以及对应的权限
     *
     * @param account 管理员信息
     * @return token
     */
    LoginSuccessVO saveIdAndPermission(AccountDO account);

    /**
     * 移除token
     *
     * @param token token
     */
    void removeToken(String token);

}
