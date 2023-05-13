/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.service;

import com.crazymakercircle.mybatis.entity.UserPO;

public interface UserService
{

    UserPO login(UserPO user);

    UserPO getById(String userid);

    int deleteById(String userid);


}
