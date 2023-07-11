package mayfly.gateway.config;

import mayfly.core.util.CollectionUtils;
import mayfly.core.util.MapUtils;
import mayfly.core.util.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 无需鉴权的url列表
 *
 * @author meilin.huang
 * @date 2021-10-13 10:50 上午
 */
@Component
@ConfigurationProperties(prefix = "auth.exclude")
public class NoAuthUrlsConfig {

    private Map<String, List<String>> services;

    public Map<String, List<String>> getServices() {
        return services;
    }

    public void setServices(Map<String, List<String>> services) {
        this.services = services;
    }

    /**
     * 路径是否匹配
     *
     * @param path 请求路径
     * @return 是否匹配
     */
    public boolean match(String service, String path) {
        if (MapUtils.isEmpty(services)) {
            return false;
        }
        List<String> urls = services.get(service);
        if (CollectionUtils.isEmpty(urls)) {
            return false;
        }
        for (String u : urls) {
            if (PathUtils.match(u, path)) {
                return true;
            }
        }
        return false;
    }
}
