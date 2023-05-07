package com.tangyh.lamp.tenant.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.tangyh.basic.base.mapper.SuperMapper;
import com.tangyh.lamp.tenant.entity.DatasourceConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 数据源
 * </p>
 *
 * @author zuihou
 * @date 2020-08-21
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface DatasourceConfigMapper extends SuperMapper<DatasourceConfig> {

    /**
     * 查询所有租户的数据源
     *
     * @param applicationName 服务名
     * @param status          状态
     * @param connectType     连接类型
     * @return
     */
    @Select("SELECT dc.id,dc.create_time,dc.created_by,dc.update_time,dc.updated_by, " +
            "       dc.name, dc.username, dc.password, dc.url, dc.driver_class_name driverClassName, t.code as pool_name " +
            " from c_datasource_config dc INNER JOIN c_tenant_datasource_config tdc on dc.id = tdc.datasource_config_id " +
            "INNER JOIN c_tenant t on t.id = tdc.tenant_id " +
            "where tdc.application = #{applicationName} " +
            "and t.`status` = #{status} and t.connect_type = #{connectType} ")
    List<DatasourceConfig> listByApplication(@Param("applicationName") String applicationName, @Param("status") String status,
                                             @Param("connectType") String connectType);
}
