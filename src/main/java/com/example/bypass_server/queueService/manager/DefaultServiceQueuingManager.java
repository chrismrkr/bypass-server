package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.factory.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultServiceQueuingManager<ResponseType> implements ServiceQueuingManager {
    private ServiceQueuingEventProducer queuedEventPublisher;
    private DeferredServiceQueuingEventHolder<ResponseType> deferredResultHolder;
    private ServiceQueuingEventResultListener queuedEventResultListener;

    private static final long TIMEOUT_MILLIS = 5000L;
    private static final String TIMEOUT_MSG = "TIME_OUT";

    @Builder
    private DefaultServiceQueuingManager(ServiceQueuingEventProducer queuedEventPublisher, DeferredServiceQueuingEventHolder<ResponseType> deferredResultHolder, ServiceQueuingEventResultListener queuedEventResultListener) {
        this.queuedEventPublisher = queuedEventPublisher;
        this.deferredResultHolder = deferredResultHolder;
        this.queuedEventResultListener = queuedEventResultListener;
    }

    @Override
    public DeferredResult<ResponseType> execute(ServiceQueuingEventResultHandler<ResponseType> messageHandler, String partitioningKey, Object target, String method, Object... param) {
        DeferredResult<ResponseType> request = new DeferredResult<>(TIMEOUT_MILLIS, TIMEOUT_MSG);
        Long requestId = ThreadLocalRandom.current().nextLong();
        this.deferredResultHolder.save(requestId, request);

        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(messageHandler, "handleMessage");
        listenerAdapter.afterPropertiesSet();
        this.queuedEventResultListener
                .listenToChannel(Long.toString(requestId), listenerAdapter);

        ServiceQueuingDetails details = ServiceQueuingDetails.builder()
                .requestId(requestId)
                .target(target)
                .method(method)
                .parameters(param)
                .build();
        this.queuedEventPublisher
                .publish(partitioningKey, details);

        return request;
    }

}
