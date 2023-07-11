import RouterParent from '@/views/layout/routerView/parent.vue';

export const imports = {
    'RouterParent': RouterParent,
    "Home": () => import('@/views/home/index.vue'),
    'Personal': () => import('@/views/personal/index.vue'),
    "ResourceList": () => import('@/views/system/resource'),
    "RoleList": () => import('@/views/system/role'),
    "AccountList": () => import('@/views/system/account'),
    "LogList": () => import('@/views/system/log'),
    "ServiceList": () => import('@/views/system/service'),
    "ApiList": () => import('@/views/system/api/ApiList.vue'),
}