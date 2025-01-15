package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceQueuingEventSubscriber implements ServiceQueuingDetailsSubscriber {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    @KafkaListener(
            topics = {"#{serviceQueuingTopicConsumerConfig.topicName}"},
            groupId = "#{serviceQueuingTopicConsumerConfig.groupId}",
            containerFactory = "serviceQueuingTopicKafkaConsumerFactory"
    )
    public void subscribeServiceQueueDetails(ConsumerRecord<String, ServiceQueuingDetails> records, Acknowledgment ack) {
        /**
         * TODO. 비즈니스 로직 실행
         */
        ServiceQueuingDetails serviceQueuingDetails = records.value();
        log.info("[DO BUSINESS] {}'s Business Method {} Should Be Implemented", records.key(), serviceQueuingDetails.getMethod());
        /**
         * TODO. Redis PUB
         */
        Long l = redisTemplate.convertAndSend(Long.toString(serviceQueuingDetails.getRequestId()), Long.toString(serviceQueuingDetails.getRequestId()));
        ack.acknowledge();
    }
}
