package com.arnold.core;

import com.arnold.common.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ConfigLoader {

    public static final String CONFIG_FILE = "gateway.properties";

    public static final String ENV_PREFIX = "GATEWAY_";

    public static final String JVM_PREFIX = "gateway.";

    private Config config;

    public static Config getConfig(){
        return instance.config;
    }

    //单例模式
    private static final ConfigLoader instance = new ConfigLoader();

    public static ConfigLoader getInstance(){
        return instance;
    }

    private ConfigLoader(){

    }

    public Config load(String[] args){
        //默认值
        config = new Config();

        //配置文件
        loadFromConfigFile();

        //环境变量
        loadFromEnv();

        //jvm参数
        loadFromJvm();

        //运行参数
        loadFromArgs(args);

        return config;
    }

    private void loadFromArgs(String[] args) {
        Properties props = new Properties();
        for (String arg : args) {
            String key = arg.startsWith("--") ? arg.substring(2) : arg;
            String[] parts = key.split("=");
            if (parts.length == 2) {
                props.setProperty(parts[0], parts[1]);
            }
        }
        PropertiesUtils.properties2Object(props, config);

    }

    private void loadFromJvm() {
        Properties properties = System.getProperties();
        PropertiesUtils.properties2Object(properties, config, JVM_PREFIX);
    }

    private void loadFromEnv() {
        Map<String, String> env = System.getenv();
        Properties properties = new Properties();
        properties.putAll(env);
        PropertiesUtils.properties2Object(properties, config, ENV_PREFIX);
    }

    private void loadFromConfigFile() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        if (resourceAsStream == null){
            throw new RuntimeException("配置文件未找到:" + CONFIG_FILE);
        }

        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
            PropertiesUtils.properties2Object(properties, config);
        } catch (Exception e) {
            log.warn("读取配置文件失败:" + CONFIG_FILE, e);
        } finally {
            try {
                resourceAsStream.close();
            } catch (Exception e) {
                log.warn("关闭配置文件流失败:" + CONFIG_FILE, e);
            }
        }
    }


}
