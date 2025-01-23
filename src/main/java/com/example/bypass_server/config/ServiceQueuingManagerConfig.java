package com.example.bypass_server.config;

import com.example.bypass_server.bypassTest.controller.BypassTestController;
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
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class ServiceQueuingManagerConfig {
    private final ServiceQueuingEventProducer queuedEventPublisher;
    private final ServiceQueuingEventResultListener queuedEventResultListener;
    private final ApplicationContext applicationContext;
    private final RedisTemplate<String, Object> genericRedisTemplate;

    @Bean
    public DeferredServiceQueuingEventHolder<BypassTestController.BypassTestResponseDTO> deferredResultHolder() {
        return new DeferredServiceQueuingEventHolder<BypassTestController.BypassTestResponseDTO>();
    }
    @Bean
    public ServiceQueuingManager<BypassTestController.BypassTestResponseDTO> defaultServiceQueuingManager() {
        DefaultServiceQueuingManagerFactory<BypassTestController.BypassTestResponseDTO> factory = new DefaultServiceQueuingManagerFactory<>(
                deferredResultHolder(),
                queuedEventPublisher, queuedEventResultListener
        );
        return factory.createManager();
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
        KafkaServiceQueuingEventSubscriberFactory<BypassTestController.BypassTestResponseDTO> factory = new KafkaServiceQueuingEventSubscriberFactory<>(
                applicationServiceExecutor(),
                serviceQueuingResultPublisher()
        );
        return factory.createInstance();
    }
}
