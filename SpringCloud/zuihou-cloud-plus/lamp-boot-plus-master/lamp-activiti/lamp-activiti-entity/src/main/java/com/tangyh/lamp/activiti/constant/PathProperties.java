package com.tangyh.lamp.activiti.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 各类地址相关配置
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@Component
@ConfigurationProperties(prefix = PathProperties.PATH)
public class PathProperties {
    /**
     * 注入配置地址
     */
    public static final String PATH = "setting.path";

    /**
     * 服务器上传路径
     */
    private String uploadPath = "/data/projects/uploadfile/temp/";
}
