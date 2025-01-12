package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceQueuingDetailsSubscriberImpl implements ServiceQueuingDetailsSubscriber {
    @Override
    @KafkaListener(
            topics = {"#{serviceQueuingTopicConsumerConfig.topicName}"},
            groupId = "#{serviceQueuingTopicConsumerConfig.groupId}",
            containerFactory = "serviceQueuingTopicKafkaConsumerFactory"
    )
    public void subscribeServiceQueueDetails(ConsumerRecord<String, ServiceQueuingDetails> records, Acknowledgment ack) {
        log.info("[Kafka SUBSCRIBE] {} consumed", records.key());
    }
}
