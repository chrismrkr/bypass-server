package com.example.bypass_server.medium.queueService.redis;

import com.example.bypass_server.queueService.service.port.ServiceQueuingEventResultListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootTest
public class ServiceQueuingEventRedisListenerTest {
    @Autowired
    ServiceQueuingEventResultListener redisPubSubListener;
    @Autowired
    RedisTemplate<String, String> redisStringTemplate;

    @Test
    void redis_PUB_SUB_테스트() {
        TestMessageListener messageListener = new TestMessageListener();
        // given
        String channelName = "test-channel";
        redisPubSubListener.listenToChannel(channelName, messageListener.createAdapter());

        // when
        Long l = redisStringTemplate.convertAndSend(channelName, "hello world!");

        // then
    }

    @Slf4j
    private static class TestMessageListener {
        public void handleMessage(String message) {
            log.info("[Message Received] {}", message);
        }
        public MessageListenerAdapter createAdapter() {
            return new MessageListenerAdapter(this, "handleMessage"); // handleMessage 메서드로 메시지 처리
        }
    }
}
