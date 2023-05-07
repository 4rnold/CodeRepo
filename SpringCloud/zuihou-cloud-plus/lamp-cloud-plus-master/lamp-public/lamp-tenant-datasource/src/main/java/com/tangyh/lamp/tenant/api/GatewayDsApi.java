package com.tangyh.lamp.tenant.api;

import com.tangyh.basic.base.R;
import com.tangyh.lamp.tenant.dto.DataSourcePropertyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 网关服务初始化数据源
 * <p>
 * 若你使用的是zuul:
 * 1. 将下面的 lamp-gateway-server 改成 lamp-zuul-server ！！！！
 * 2. 将path 改成 /api/gate/ds
 *
 * @author zuihou
 * @date 2020年04月05日18:18:26
 */
//@FeignClient(name = "${lamp.feign.gateway-server:lamp-zuul-server}", path = "/api/gate/ds")
@FeignClient(name = "${lamp.feign.gateway-server:lamp-gateway-server}", path = "/ds")
public interface GatewayDsApi {

    /**
     * 初始化数据源
     *
     * @param tenantConnect
     * @return
     */
    @RequestMapping(value = "/initConnect", method = RequestMethod.POST)
    R<Boolean> initConnect(@RequestBody DataSourcePropertyDTO tenantConnect);

    /**
     * 删除数据源
     *
     * @param tenant
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    R<Boolean> remove(@RequestParam(value = "tenant") String tenant);
}
