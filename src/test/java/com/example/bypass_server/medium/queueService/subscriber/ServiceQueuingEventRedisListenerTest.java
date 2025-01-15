package com.example.bypass_server.medium.queueService.subscriber;

import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingDetailsEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    void redis_PUB_메세지를_SUB하여_handle할_수_있음() {
        // given
        final boolean[] flag = {false};
        ServiceQueuingDetailsEventHandler eventHandler = new ServiceQueuingDetailsEventHandler() {
            @Override
            public void handleMessage(String requestId) {
                log.info("[REDIS SUB] success");
                flag[0] = true;
            }
        };
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(eventHandler, "handleMessage");
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
        Assertions.assertTrue(flag[0]);
    }
}
