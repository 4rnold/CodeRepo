package com.itheima.cache.impl;

import com.itheima.cache.CacheStrategy;
import com.itheima.conditions.RedisCondition;
import com.itheima.domain.ConfigInfo;
import com.itheima.utils.KryoSerializeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis缓存策略的实现
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
@Conditional(RedisCondition.class)
public class RedisCacheStrategy implements CacheStrategy,InitializingBean{

    @Autowired
    private RedisTemplate redisTemplate;//此处不要指定泛型

    @Override
    public void saveCache(ConfigInfo configInfo) {
        //1.判断当前要缓存的对象是否已经被缓存过
        boolean isCached = redisTemplate.hasKey(configInfo.getId());
        if(isCached){
            //2.移除缓存
            this.removeCache(configInfo.getId());
        }
        //3.缓存到redis
        redisTemplate.opsForValue().set(configInfo.getId(), KryoSerializeUtil.serialize(configInfo));
    }

    @Override
    public void removeCache(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public ConfigInfo findCache(String key) {
        //1.根据key，从缓存服务器中获取数据
        byte[] bytes = (byte[])redisTemplate.opsForValue().get(key);
        //2.把字节数组转成ConfigInfo对象
        ConfigInfo configInfo = KryoSerializeUtil.unserialize(bytes,ConfigInfo.class);
        //3.返回
        return configInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }
}
