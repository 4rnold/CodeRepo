package mayfly.auth.biz.permission;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.ArrayUtils;

import java.util.Set;

/**
 * @author meilin.huang
 * @date 2021-08-28 5:02 下午
 */
@Getter
@Setter
public class SysLoginAccount extends LoginAccount {

    /**
     * 权限code数组
     */
    private Set<String> permissions;

    /**
     * 用户可访问的api列表
     */
    private String[] apis;

    /**
     * 账号角色
     */
    private String[] roles;

//    /**
//     * 判断该账号是否拥有该权限码
//     *
//     * @param permissionInfo 权限信息
//     * @return true：有
//     */
//    public boolean hasPermission(PermissionInfo permissionInfo) {
//        if (!permissionInfo.isRequireCode()) {
//            return true;
//        }
//        if (!hasRole(permissionInfo.getRoles())) {
//            return false;
//        }
//        if (ArrayUtils.isEmpty(permissions)) {
//            return true;
//        }
//
//        // 校验权限code
//        String permissionCode = permissionInfo.getPermissionCode();
//        if (StringUtils.isEmpty(permissionCode)) {
//            return false;
//        }
//        return ArrayUtils.contains(this.permissions, permissionCode);
//    }

    /**
     * 判断账号的角色是否存在于指定roles中
     *
     * @param roles 角色数组
     * @return true:账号拥有该角色
     */
    public boolean hasRole(String[] roles) {
        String[] selfRoles = this.roles;
        if (ArrayUtils.isEmpty(selfRoles)) {
            return true;
        }
        for (String role : roles) {
            if (ArrayUtils.contains(selfRoles, role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermission(String code) {
        return permissions.contains(code);
    }

    public SysLoginAccount permissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
