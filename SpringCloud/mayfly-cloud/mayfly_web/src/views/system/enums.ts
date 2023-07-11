import { Enum } from '@/common/Enum'

/**
 * 枚举类
 */
export default {
    // 资源类型枚举
    ResourceTypeEnum: new Enum().add('MENU', '菜单', 1).add('PERMISSION', '权限', 2),
    // 账号状态枚举
    accountStatus: new Enum().add('ENABLE', '正常', 1).add('DISABLE', '禁用', 0),
    logType: new Enum().add('SYS_LOG', '系统', 4).add('ERR_LOG', '异常', 5),
    ApiCodeTypeEnum: new Enum().add('ROLE', '角色', 1).add('RESOURCE', '资源', 2),
}