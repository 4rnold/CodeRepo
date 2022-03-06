package com.arnold.ecommerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.arnold.ecommerce.account.AddressInfo;
import com.arnold.ecommerce.common.TableId;
import com.arnold.ecommerce.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AddressServiceImplTest extends BaseTest{
    @Autowired
    private IAddressService addressService;

    @Test
    public void createAddressInfo() {
        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
        addressItem.setUsername("arnold");
        addressItem.setPhone("1333333333");
        addressItem.setProvince("上海市");
        addressItem.setCity("上海市");
        addressItem.setAddressDetail("陆家嘴");
        log.info("test create address info: [{}]", JSON.toJSONString(
                addressService.createAddressInfo(
                        new AddressInfo(loginUserInfo.getId(),
                                Collections.singletonList(addressItem))
                )
        ));
    }

    @Test
    public void getCurrentAddressInfo() {
        log.info("test get current user info: [{}]", JSON.toJSONString(
                addressService.getCurrentAddressInfo()
        ));
    }

    @Test
    public void getAddressInfoById() {
        log.info("test get address info by id: [{}]", JSON.toJSONString(
                addressService.getAddressInfoById(10L)
        ));
    }

    @Test
    public void getAddressInfoByTableId() {
        log.info("test get address info by table id: [{}]", JSON.toJSONString(
                addressService.getAddressInfoByTableId(
                        new TableId(Collections.singletonList(new TableId.Id(10L)))
                )
        ));
    }
}