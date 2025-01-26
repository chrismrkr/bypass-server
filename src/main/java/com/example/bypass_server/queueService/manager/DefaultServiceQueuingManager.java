package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultServiceQueuingManager<ResponseType> implements ServiceQueuingManager<ResponseType> {
    private String topic;
    private DeferredServiceQueuingEventHolder<ResponseType> deferredResultHolder;
    private ServiceQueuingEventProducer queuedEventPublisher;
    private ServiceQueuingDetailsSubscriber<ResponseType> queuedEventSubscriber;
    private ServiceQueuingEventResultListener queuedEventResultListener;

    private static final long TIMEOUT_MILLIS = 5000L;
    private static final String TIMEOUT_MSG = "TIME_OUT";

    public DefaultServiceQueuingManager(String topic, DeferredServiceQueuingEventHolder<ResponseType> deferredResultHolder, ServiceQueuingEventProducer queuedEventPublisher, ServiceQueuingDetailsSubscriber<ResponseType> queuedEventSubscriber, ServiceQueuingEventResultListener queuedEventResultListener) {
        this.topic = topic;
        this.deferredResultHolder = deferredResultHolder;
        this.queuedEventPublisher = queuedEventPublisher;
        this.queuedEventSubscriber = queuedEventSubscriber;
        this.queuedEventResultListener = queuedEventResultListener;
    }

    @Override
    public DeferredResult<ResponseType> execute(ServiceQueuingEventResultHandler<ResponseType> messageHandler, String partitioningKey, Object target, String method, Object... param) {
        DeferredResult<ResponseType> request = new DeferredResult<>(TIMEOUT_MILLIS, TIMEOUT_MSG);
        Long requestId = ThreadLocalRandom.current().nextLong();
        this.deferredResultHolder.save(requestId, request);

        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(messageHandler, "handleMessage");
        listenerAdapter.afterPropertiesSet();
        this.queuedEventResultListener.listenToChannel(Long.toString(requestId), listenerAdapter);

        ServiceQueuingDetails details = ServiceQueuingDetails.builder()
                .requestId(requestId)
                .target(target)
                .method(method)
                .parameters(param)
                .build();
        this.queuedEventPublisher.publish(this.topic, partitioningKey, details);

        return request;
    }

}
