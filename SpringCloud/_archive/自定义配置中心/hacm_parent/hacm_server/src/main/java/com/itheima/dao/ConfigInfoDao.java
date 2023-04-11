package com.itheima.dao;

import com.itheima.domain.ConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 配置新的持久层
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface ConfigInfoDao extends JpaRepository<ConfigInfo,String>, JpaSpecificationExecutor<ConfigInfo> {
}
