package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;
import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KafkaServiceQueuingEventSubscriber<ResponseType> implements ServiceQueuingDetailsSubscriber<ResponseType> {
    private ServiceQueuingResultPublisher<ResponseType> resultPublisher;
    private ApplicationServiceExecutor applicationServiceExecutor;
    private ConsumerFactory<String, ServiceQueuingDetails> consumerFactory;

    public KafkaServiceQueuingEventSubscriber(ServiceQueuingResultPublisher<ResponseType> resultPublisher, ApplicationServiceExecutor applicationServiceExecutor, ConsumerFactory<String, ServiceQueuingDetails> consumerFactory) {
        this.resultPublisher = resultPublisher;
        this.applicationServiceExecutor = applicationServiceExecutor;
        this.consumerFactory = consumerFactory;
    }

    public KafkaMessageListenerContainer<String, ServiceQueuingDetails> createListener(String topic, String groupId) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setGroupId(groupId);
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);
        containerProperties.setMessageListener((AcknowledgingMessageListener<String, ServiceQueuingDetails>) (record, ack) -> {
            try {
                ServiceQueuingDetails details = record.value();
                log.info("[Queued Event Subscribed] {} {}", details.getTarget(), details.getMethod());
                ResponseType result = (ResponseType) applicationServiceExecutor.execute(details.getTarget(), details.getMethod(), details.getParameters());
                resultPublisher.publishResult(Long.toString(details.getRequestId()),
                        QueuedServiceResult.<ResponseType>builder()
                                .requestId(details.getRequestId())
                                .response(result)
                                .build()
                );
                Objects.requireNonNull(ack).acknowledge();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

//    @KafkaListener(
//            topics = {"#{serviceQueuingTopicConsumerConfig.topicName}"},
//            groupId = "#{serviceQueuingTopicConsumerConfig.groupId}",
//            containerFactory = "serviceQueuingTopicKafkaConsumerFactory"
//    )
    public void subscribeServiceQueueDetails(ConsumerRecord<String, ServiceQueuingDetails> records, Acknowledgment ack) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ServiceQueuingDetails details = records.value();
        ResponseType result = (ResponseType) applicationServiceExecutor.execute(details.getTarget(), details.getMethod(), details.getParameters());
        resultPublisher.publishResult(Long.toString(details.getRequestId()),
                QueuedServiceResult.<ResponseType>builder()
                        .requestId(details.getRequestId())
                        .response(result)
                        .build()
                );
        ack.acknowledge();
    }
}
