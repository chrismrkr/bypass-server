package com.example.bypass_server.medium.queueService.redis;

import com.example.bypass_server.bypassTest.handler.RedisMessageHandler;
import com.example.bypass_server.queueService.service.port.ServiceQueuingEventResultListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootTest
@Slf4j
public class ServiceQueuingEventRedisListenerTest {
    @Autowired
    ServiceQueuingEventResultListener redisPubSubListener;
    @Autowired
    RedisTemplate<String, String> redisStringTemplate;

    @Test
    void redis_PUB_SUB_테스트() {
        // given
        RedisMessageHandler messageHandler = new RedisMessageHandler();
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(messageHandler, "handleMessage");
        messageListenerAdapter.afterPropertiesSet();
        // MessageListenerAdapter를 Bean으로 등록하지 않으면 반드시 afterPropertiesSet 호출이 필요함
        String channelName = "test-channel";
        redisPubSubListener.listenToChannel(channelName, messageListenerAdapter);

        // when
        Long l = redisStringTemplate.convertAndSend(channelName, "hello world!");

        // then
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("END");
    }
}
