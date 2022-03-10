package com.arnold.ecommerce.service;

import com.arnold.ecommerce.account.AddressInfo;
import com.arnold.ecommerce.common.TableId;

/**
 * <h1>用户地址相关服务接口定义</h1>
 * */
public interface IAddressService {

    /**
     * <h2>创建用户地址信息</h2>
     * */
    TableId createAddressInfo(AddressInfo addressInfo);

    /**
     * <h2>获取当前登录的用户地址信息</h2>
     * */
    AddressInfo getCurrentAddressInfo();

    /**
     * <h2>通过 id 获取用户地址信息, id 是 EcommerceAddress 表的主键</h2>
     * */
    AddressInfo getAddressInfoById(Long id);

    /**
     * <h2>通过 TableId 获取用户地址信息</h2>
     * */
    AddressInfo getAddressInfoByTableId(TableId tableId);
}
