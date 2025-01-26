package com.example.bypass_server.queueService.subscriber;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

import java.lang.reflect.InvocationTargetException;

public interface ServiceQueuingDetailsSubscriber<T> {
    KafkaMessageListenerContainer<String, ServiceQueuingDetails> createListener(String topic, String groupId);
}
