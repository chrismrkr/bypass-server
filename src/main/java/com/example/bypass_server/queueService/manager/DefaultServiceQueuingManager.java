package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.adaptor.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultServiceQueuingManager implements ServiceQueuingManager {
    private final ServiceQueuingAdaptor serviceQueuingAdaptor;
    private static final long TIMEOUT_MILLIS = 5000L;
    private static final String TIMEOUT_MSG = "TIME_OUT";
    @Override
    public DeferredResult<Object> execute(ServiceQueuingEventResultHandler messageHandler, String partitioningKey, Object target, String method, Object... param) {
        DeferredResult<Object> request = new DeferredResult<>(TIMEOUT_MILLIS, TIMEOUT_MSG);
        Long requestId = ThreadLocalRandom.current().nextLong();
        this.serviceQueuingAdaptor.getDeferredResultHolderWriter()
                .save(requestId, request);

        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(messageHandler, "handleMessage");
        listenerAdapter.afterPropertiesSet();
        this.serviceQueuingAdaptor.getServiceQueuingEventResultListener()
                .listenToChannel(Long.toString(requestId), listenerAdapter);

        ServiceQueuingDetails details = ServiceQueuingDetails.builder()
                .requestId(requestId)
                .target(target)
                .method(method)
                .parameters(param)
                .build();
        this.serviceQueuingAdaptor.getServiceQueuingEventProducer()
                .publish(partitioningKey, details);

        return request;
    }

}
