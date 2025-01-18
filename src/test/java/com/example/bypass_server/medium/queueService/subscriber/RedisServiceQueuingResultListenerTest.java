package com.example.bypass_server.medium.queueService.subscriber;

import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.publisher.RedisServiceQueuingResultPublisher;
import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
@Slf4j
public class RedisServiceQueuingResultListenerTest {
    @Autowired
    ServiceQueuingEventResultListener redisPubSubListener;
    @Autowired
    RedisServiceQueuingResultPublisher queuingResultPublisher;

    @Test
    void redis_PUB_메세지를_SUB하여_handle할_수_있음() {
        // given
        final boolean[] flag = {false};
        ServiceQueuingEventResultHandler eventHandler = new ServiceQueuingEventResultHandler() {
            @Override
            public void handleMessage(QueuedServiceResult message) {
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
        queuingResultPublisher.publishResult(
                    channelName,
                    QueuedServiceResult.builder()
                            .requestId(1L)
                            .response("Hello World")
                            .build()
                );

        // then
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assertions.assertTrue(flag[0]);
    }
}
