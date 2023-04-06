package com.heima.storage.mapper;


import com.heima.modules.po.AuthenticationPO;

import java.util.List;

public interface AuthenticationMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuthenticationPO record);

    int insertSelective(AuthenticationPO record);

    AuthenticationPO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuthenticationPO record);

    int updateByPrimaryKey(AuthenticationPO record);

    List<AuthenticationPO> selectList(AuthenticationPO record);

    AuthenticationPO selectByPhone(String phone);
}