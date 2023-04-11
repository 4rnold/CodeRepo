package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新用户的粉丝数
     * @param userid
     * @param num
     */
    @Modifying
    @Query("update User  set fanscount = fanscount + ?2 where id = ?1 ")
    void updateFanscount(String userid, int num);

    /**
     * 更新用户的关注数
     * @param userid
     * @param num
     */
    @Modifying
    @Query("update User set followcount = followcount+?2 where id = ?1 ")
    void updateFollowcount(String userid, int num);
}
