package com.example.bypass_server.config;

import com.example.bypass_server.bypassTest.controller.BypassTestController;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.factory.DefaultServiceQueuingManagerFactory;
import com.example.bypass_server.queueService.factory.KafkaServiceQueuingEventSubscriberFactory;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.manager.DefaultServiceQueuingManager;
import com.example.bypass_server.queueService.publisher.RedisServiceQueuingResultPublisher;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.executor.DefaultApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.impl.KafkaServiceQueuingEventSubscriber;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class ServiceQueuingManagerConfig {
    private final ServiceQueuingEventProducer queuedEventPublisher;
    private final ServiceQueuingEventResultListener queuedEventResultListener;
    private final ConsumerFactory<String, ServiceQueuingDetails> queuedEventConsumerFactory;
    private final ApplicationContext applicationContext;
    private final RedisTemplate<String, Object> genericRedisTemplate;
    private final Environment env;

    @Bean
    public ServiceQueuingManager<BypassTestController.BypassTestResponseDTO> defaultServiceQueuingManager() {
        DefaultServiceQueuingManagerFactory<BypassTestController.BypassTestResponseDTO> factory = new DefaultServiceQueuingManagerFactory<>(
                env.getProperty("spring.kafka.topic.service-queuing.topic-name"),
                deferredResultHolder(),
                queuedEventPublisher,
                kafkaServiceQueuingEventSubscriber(),
                queuedEventResultListener
        );
        return factory.createManager();
    }
    @Bean
    public DeferredServiceQueuingEventHolder<BypassTestController.BypassTestResponseDTO> deferredResultHolder() {
        return new DeferredServiceQueuingEventHolder<BypassTestController.BypassTestResponseDTO>();
    }
    @Bean
    public DefaultApplicationServiceExecutor applicationServiceExecutor() {
        return new DefaultApplicationServiceExecutor(applicationContext);
    }
    @Bean
    public RedisServiceQueuingResultPublisher<BypassTestController.BypassTestResponseDTO> serviceQueuingResultPublisher() {
        return new RedisServiceQueuingResultPublisher<>(genericRedisTemplate);
    }
    @Bean
    public KafkaServiceQueuingEventSubscriber<BypassTestController.BypassTestResponseDTO> kafkaServiceQueuingEventSubscriber() {
        return new KafkaServiceQueuingEventSubscriber<>(
                serviceQueuingResultPublisher(),
                applicationServiceExecutor(),
                queuedEventConsumerFactory
        );
    }

    @Bean
    public KafkaMessageListenerContainer<String, ServiceQueuingDetails> defaultQueuedEventListener() {
        String topic = env.getProperty("spring.kafka.topic.service-queuing.topic-name");
        String groupId = env.getProperty("spring.kafka.topic.service-queuing.group-id");
        KafkaMessageListenerContainer<String, ServiceQueuingDetails> listener = kafkaServiceQueuingEventSubscriber().createListener(topic, groupId);
        listener.start();
        return listener;
    }
}
