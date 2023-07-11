import Api from '@/common/Api';

export const personApi = {
    accountInfo: Api.create("/sys/personal", 'get'),
    updateAccount: Api.create("/sys/personal", 'put'),
    getMsgs: Api.create("/sys/accounts/msgs", 'get'),
}

