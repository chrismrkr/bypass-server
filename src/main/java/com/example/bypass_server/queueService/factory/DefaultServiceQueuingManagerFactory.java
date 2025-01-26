package com.example.bypass_server.queueService.factory;

import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.manager.DefaultServiceQueuingManager;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class DefaultServiceQueuingManagerFactory<T> implements ServiceQueuingManagerFactory<T> {
    private String topic;
    private DeferredServiceQueuingEventHolder<T> deferredResultHolder;
    private ServiceQueuingEventProducer queuedEventPublisher;
    private ServiceQueuingDetailsSubscriber<T> queuedEventSubscriber;
    private ServiceQueuingEventResultListener queuedEventResultListener;

    public DefaultServiceQueuingManagerFactory(String topic, DeferredServiceQueuingEventHolder<T> deferredResultHolder, ServiceQueuingEventProducer queuedEventPublisher, ServiceQueuingDetailsSubscriber<T> queuedEventSubscriber, ServiceQueuingEventResultListener queuedEventResultListener) {
        this.topic = topic;
        this.deferredResultHolder = deferredResultHolder;
        this.queuedEventPublisher = queuedEventPublisher;
        this.queuedEventSubscriber = queuedEventSubscriber;
        this.queuedEventResultListener = queuedEventResultListener;
    }

    @Override
    public ServiceQueuingManager<T> createManager() {
        return new DefaultServiceQueuingManager<T>(
                topic,
                deferredResultHolder,
                queuedEventPublisher, queuedEventSubscriber,
                queuedEventResultListener
                );
    }
}
