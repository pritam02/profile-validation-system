package net.intuit.profilevalidation.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Component
public class CacheUtil {
    @Autowired
    private JedisPool jedisPool;

    public boolean set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("failed to write to cache");
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }

    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("failed to get from cache");
            value = null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }
}
