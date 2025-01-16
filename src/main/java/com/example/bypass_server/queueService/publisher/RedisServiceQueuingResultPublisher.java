package com.example.bypass_server.queueService.publisher;

import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisServiceQueuingResultPublisher implements ServiceQueuingResultPublisher {
    private final RedisTemplate<String, Object> genericRedisTemplate;

    @Autowired
    public RedisServiceQueuingResultPublisher(@Qualifier("genericRedisTemplate") RedisTemplate<String, Object> redisStringTemplate) {
        this.genericRedisTemplate = redisStringTemplate;
    }

    @Override
    public void publishResult(String channel, Object message) {
        genericRedisTemplate.convertAndSend(channel, message);
    }
}
