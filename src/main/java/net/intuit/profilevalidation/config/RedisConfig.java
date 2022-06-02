package net.intuit.profilevalidation.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@PropertySource("classpath:redis.properties")
@Configuration
@Getter
@Setter
public class RedisConfig {
    @Value("${redisIP}")
    private String redisIP;
    @Value("${redisPort}")
    private Integer redisPort;
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return new JedisPool(config, redisIP, redisPort);
    }
}
