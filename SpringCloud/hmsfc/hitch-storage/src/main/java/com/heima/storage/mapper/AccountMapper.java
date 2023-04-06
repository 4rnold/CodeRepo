package com.heima.storage.mapper;


import com.heima.modules.po.AccountPO;

import java.util.List;

public interface AccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountPO record);

    int insertSelective(AccountPO record);

    AccountPO selectByPrimaryKey(String id);


    int updateByPrimaryKeySelective(AccountPO record);

    int updateByPrimaryKey(AccountPO record);

    AccountPO checkLogin(AccountPO accountPO);
}