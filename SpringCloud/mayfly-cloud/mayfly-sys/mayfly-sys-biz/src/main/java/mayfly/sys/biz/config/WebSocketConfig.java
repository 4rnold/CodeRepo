package mayfly.sys.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-27 6:52 PM
 */
@Configuration(proxyBeanMethods = false)
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
