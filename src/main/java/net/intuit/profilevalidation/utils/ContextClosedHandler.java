package net.intuit.profilevalidation.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;


@Service
@Slf4j
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    private JedisPool jedisPool;

    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("application closing");
        jedisPool.close();
    }
}
