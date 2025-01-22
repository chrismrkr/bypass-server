package com.example.bypass_server.queueService.factory;

import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.manager.DefaultServiceQueuingManager;
import com.example.bypass_server.queueService.manager.ServiceQueuingManager;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class DefaultServiceQueuingManagerFactory {
    private DeferredServiceQueuingEventHolder deferredResultHolder;
    private ServiceQueuingEventProducer queuedEventPublisher;
    private ServiceQueuingEventResultListener queuedEventResultListener;

    @Builder
    public DefaultServiceQueuingManagerFactory(DeferredServiceQueuingEventHolder deferredResultHolder, ServiceQueuingEventProducer queuedEventPublisher, ServiceQueuingEventResultListener queuedEventResultListener) {
        this.deferredResultHolder = deferredResultHolder;
        this.queuedEventPublisher = queuedEventPublisher;
        this.queuedEventResultListener = queuedEventResultListener;
    }

    public DefaultServiceQueuingManager<?> createDefaultServiceQueuingManager(Class<?> responseType) {
        return DefaultServiceQueuingManager.builder()
                .deferredResultHolder(deferredResultHolder)
                .queuedEventPublisher(queuedEventPublisher)
                .queuedEventResultListener(queuedEventResultListener)
                .build();
    }
}
