package com.crazymakercircle.json;

import com.crazymakercircle.config.SystemConfig;
import lombok.extern.slf4j.Slf4j;

import static com.crazymakercircle.util.JvmUtil.isClassPresent;

@Slf4j
public abstract class JsonContext implements JsonStrategy {


    private  static volatile JsonStrategy strategy;

    private JsonContext() {
    }

    private static final String CLASS_TYPE_JACKSON = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String CLASS_TYPE_FASTJSON = "com.alibaba.fastjson.JSON";
    private static final String CLASS_TYPE_GSON = "com.google.gson.Gson";

    /**
     * json的类型: gson/fastjson/Jackson
     */
    private static final String JACKSON = "Jackson";
    private static final String FASTJSON = "fastjson";
    private static final String GSON = "gson";

    private static JsonStrategy loadFromConfig() {

        String jsonType = SystemConfig.JSON_STRATEGY;

        switch (jsonType) {
            case JACKSON:
                if (isClassPresent(CLASS_TYPE_JACKSON)) {
                    log.info("used jackson");
                    return new JacksonJsonStrategy();
                } else {
                    log.error("jackson not found");
                    throw new RuntimeException("未找到jackson的依赖");
                }
            case FASTJSON:
                if (isClassPresent(CLASS_TYPE_FASTJSON)) {
                    log.info("used fastjson");
                    return new FastJsonStrategy();
                } else {
                    log.error("fastjson not found");
                    throw new RuntimeException("未找到fastjson的依赖");
                }
            case GSON:
                if (isClassPresent(CLASS_TYPE_GSON)) {
                    log.info("used gson");
                    return new GsonStrategy();
                } else {
                    log.error("gson not found");
                    throw new RuntimeException("未找到gson的依赖");
                }
            default:
                log.error("未找到jackson、gson或fastjson的依赖");
                throw new RuntimeException("未找到jackson、gson或fastjson的依赖");
        }
    }

    public static JsonStrategy getStrategy() {

        if (strategy == null) {

            synchronized (JsonContext.class) {

                if (strategy == null) {

                    strategy = loadFromConfig();
                }
            }
        }

        return strategy;
    }

    private void setStrategy(JsonStrategy strategy) {
        this.strategy = strategy;
    }


}