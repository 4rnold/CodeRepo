package com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter;

import com.arnold.core.redis.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.Arrays;

@Slf4j
public class RedisCountLimiter implements CountLimiter {


    public RedisCountLimiter() {
    }

    public boolean doFlowCtl(String key, int limit, int expireTime) {
        Object object = executeScript(key, limit, expireTime);
        if (object == null) {
            return true;
        }

        Long result = Long.valueOf(object.toString());
        if (0 == result) {
            return false;
        }
        return true;
    }

    public Object executeScript(String key, int limit, int expire){
        ShardedJedis shardedJedis = JedisUtil.getInstance();
        Jedis jedis = shardedJedis.getShard(key);
        String lua = buildLuaScript();
        String scriptLoad =jedis.scriptLoad(lua);
        try {
            Object result = jedis.evalsha(scriptLoad, Arrays.asList(key),
                    Arrays.asList(String.valueOf(expire), String.valueOf(limit)));
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 构造 Lua 脚本，保证线程安全。
     *
     * 当访问的是 redis 中的共享数据时，除了可以通过加锁解决，还可以 使用 lua 脚本 解决。
     *
     * redis 虽然可以支持多个客户端的并发连接，但是每个客户端的操作其实是按照顺序进行的。
     * 也就是说，如果一个客户端正在执行某个操作，其他客户端需要等待这个操作执行完毕后才能进行操作。
     *
     * 把多个操作写成一个 lua 脚本，使其具备原子性，作为一个整体执行。再由于 redis 是单线程模型，
     * 不同线程的 lua 脚本是依次执行的。也就是说，只有一个线程原子性的多个操作执行完，下一个线程才可以执行。
     * @return
     */
    private static String buildLuaScript() {
        String lua = "local num = redis.call('incr', KEYS[1])\n" +
                "if tonumber(num) == 1 then\n" +
                "\tredis.call('expire', KEYS[1], ARGV[1])\n" +
                "\treturn 1\n" +
                "elseif tonumber(num) > tonumber(ARGV[2]) then\n" +
                "\treturn 0\n" +
                "else \n" +
                "\treturn 1\n" +
                "end\n";
        return lua;
    }

}
