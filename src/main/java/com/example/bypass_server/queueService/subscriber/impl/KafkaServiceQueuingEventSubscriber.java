package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;
import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaServiceQueuingEventSubscriber implements ServiceQueuingDetailsSubscriber {
    private ServiceQueuingResultPublisher resultPublisher;
    private ApplicationServiceExecutor applicationServiceExecutor;

    @Builder
    private KafkaServiceQueuingEventSubscriber(ServiceQueuingResultPublisher resultPublisher, ApplicationServiceExecutor applicationServiceExecutor) {
        this.resultPublisher = resultPublisher;
        this.applicationServiceExecutor = applicationServiceExecutor;
    }

    @Override
    @KafkaListener(
            topics = {"#{serviceQueuingTopicConsumerConfig.topicName}"},
            groupId = "#{serviceQueuingTopicConsumerConfig.groupId}",
            containerFactory = "serviceQueuingTopicKafkaConsumerFactory"
    )
    public void subscribeServiceQueueDetails(ConsumerRecord<String, ServiceQueuingDetails> records, Acknowledgment ack) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ServiceQueuingDetails details = records.value();
        Object result = applicationServiceExecutor.execute(details.getTarget(), details.getMethod(), details.getParameters());
        resultPublisher.publishResult(Long.toString(details.getRequestId()),
                QueuedServiceResult.builder()
                        .requestId(details.getRequestId())
                        .response(result)
                        .build()
                );
        ack.acknowledge();
    }

}
