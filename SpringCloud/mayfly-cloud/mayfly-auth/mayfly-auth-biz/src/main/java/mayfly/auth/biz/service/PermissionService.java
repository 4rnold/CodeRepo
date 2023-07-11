package mayfly.auth.biz.service;

import mayfly.auth.api.model.req.AuthDTO;
import mayfly.auth.api.model.res.AuthResDTO;
import mayfly.auth.biz.contoller.vo.LoginSuccessVO;
import mayfly.auth.biz.permission.SysLoginAccount;
import mayfly.core.permission.registry.LoginAccountRegistry;
import mayfly.sys.api.model.res.AccountDTO;


/**
 * 权限服务
 *
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService extends LoginAccountRegistry<SysLoginAccount> {

    /**
     * 保存账号及权限信息
     *
     * @param account 账号信息
     * @return 登录成功信息
     */
    LoginSuccessVO saveIdAndPermission(AccountDTO account);

    /**
     * 移除token
     *
     * @param token token
     */
    void removeToken(String token);

    /**
     * 鉴权
     *
     * @param req 鉴权请求
     * @return 鉴权结果
     */
    AuthResDTO auth(AuthDTO req);

}
