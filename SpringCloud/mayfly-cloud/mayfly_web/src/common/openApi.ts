import request from './request'

export default {
    login: (param: any) => request.request('POST', '/auth/login', param, null),
    captcha: () => request.request('GET', '/auth/captcha', null, null),
    logout: (param: any) => request.request('POST', '/auth/logout/{token}', param, null),
    getMenuRoute: (param: any) => request.request('Get', '/sys/resources/account', param, null)
}