package com.crazymakercircle.mybatis.mapper;

import com.crazymakercircle.mybatis.entity.UserPO;
import com.crazymakercircle.mybatis.utility.MyMapper;
import org.apache.ibatis.annotations.Mapper;

//@Component
@Mapper
public interface UserMapper extends MyMapper<UserPO>
{
}