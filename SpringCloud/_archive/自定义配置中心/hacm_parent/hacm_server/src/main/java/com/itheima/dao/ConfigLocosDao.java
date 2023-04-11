package com.itheima.dao;

import com.itheima.domain.ConfigLocos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 记录轨迹的持久层接口
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface ConfigLocosDao extends MongoRepository<ConfigLocos,String> {

    /**
     * 根据配置信息ID查询推送轨迹，带分页
     * @param configInfoId
     * @param pageable
     * @return
     */
    Page<ConfigLocos> findByConfigInfoId(String configInfoId, Pageable pageable);

    /**
     * 根据创建时间查询轨迹
     * @param createTime
     * @return
     */
    List<ConfigLocos> findByCreateTimeLessThan(Long createTime);
}
