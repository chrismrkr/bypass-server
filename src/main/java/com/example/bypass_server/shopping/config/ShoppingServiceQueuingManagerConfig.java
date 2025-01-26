package com.example.bypass_server.shopping.config;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.factory.DefaultServiceQueuingManagerFactory;
import com.example.bypass_server.queueService.factory.KafkaServiceQueuingEventSubscriberFactory;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.publisher.RedisServiceQueuingResultPublisher;
import com.example.bypass_server.queueService.subscriber.impl.KafkaServiceQueuingEventSubscriber;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import com.example.bypass_server.shopping.controller.dto.ShoppingResponseDto;
import com.example.bypass_server.queueService.subscriber.executor.DefaultApplicationServiceExecutor;
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
public class ShoppingServiceQueuingManagerConfig {
    private final ServiceQueuingEventProducer queuedEventPublisher;
    private final ServiceQueuingEventResultListener queuedEventResultListener;
    private final ConsumerFactory<String, ServiceQueuingDetails> queuedEventConsumerFactory;
    private final ApplicationContext applicationContext;
    private final RedisTemplate<String, Object> genericRedisTemplate;
    private final Environment env;

    @Bean(name = "shoppingResponseDeferredEventHolder")
    public DeferredServiceQueuingEventHolder<ShoppingResponseDto> shoppingResponseDeferredEventHolder() {
        return new DeferredServiceQueuingEventHolder<ShoppingResponseDto>();
    }
    @Bean(name = "shoppingServiceQueuingManager")
    public ServiceQueuingManager<ShoppingResponseDto> shoppingServiceQueuingManager() {
        DefaultServiceQueuingManagerFactory<ShoppingResponseDto> factory = new DefaultServiceQueuingManagerFactory<>(
                env.getProperty("spring.kafka.topic.shopping.item.topic-name"),
                shoppingResponseDeferredEventHolder(),
                queuedEventPublisher,
                shoppingServiceQueuingEventSubscriber(),
                queuedEventResultListener
        );
        return factory.createManager();
    }

    @Bean(name = "ShoppingServiceExecutor")
    public DefaultApplicationServiceExecutor shoppingServiceExecutor() {
        return new DefaultApplicationServiceExecutor(applicationContext);
    }
    @Bean(name = "shoppingServiceResultPublisher")
    public RedisServiceQueuingResultPublisher<ShoppingResponseDto> shoppingServiceResultPublisher() {
        return new RedisServiceQueuingResultPublisher<>(genericRedisTemplate);
    }

    @Bean(name = "shoppingServiceQueuingEventSubscriber")
    public KafkaServiceQueuingEventSubscriber<ShoppingResponseDto> shoppingServiceQueuingEventSubscriber() {
        return new KafkaServiceQueuingEventSubscriber<>(
                shoppingServiceResultPublisher(),
                shoppingServiceExecutor(),
                queuedEventConsumerFactory
        );
    }

    @Bean(name = "shoppingQueuedEventListener")
    public KafkaMessageListenerContainer<String, ServiceQueuingDetails> shoppingQueuedEventListener() {
        String topic = env.getProperty("spring.kafka.topic.shopping.item.topic-name");
        String groupId = env.getProperty("spring.kafka.topic.shopping.item.group-id");
        KafkaMessageListenerContainer<String, ServiceQueuingDetails> listener = shoppingServiceQueuingEventSubscriber().createListener(topic, groupId);
        listener.start();
        return listener;
    }
}
