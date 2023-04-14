package com.itheima.dao.log;

import com.itheima.domain.LogInfo;
import org.apache.ibatis.annotations.Insert;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface LogInfoDao {

    @Insert("insert into t_log_info(id,method,action,username,create_time)values(#{id},#{method},#{action}, #{username},#{createTime})")
    int save(LogInfo logInfo);
}
