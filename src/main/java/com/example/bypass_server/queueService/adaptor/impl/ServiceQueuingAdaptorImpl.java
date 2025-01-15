package com.example.bypass_server.queueService.adaptor.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.adaptor.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.adaptor.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingDetailsEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ServiceQueuingAdaptorImpl implements ServiceQueuingAdaptor {
    private final ServiceQueuingEventProducer serviceQueuingEventProducer;
    private final DeferredResultHolderWriter<ServiceQueuingDetails> deferredResultHolder;
    private final ServiceQueuingEventResultListener serviceQueuingEventResultListener;

    @Override
    public DeferredResult<ServiceQueuingDetails> queueService(String clientUniqueKey, String method, Object param, ServiceQueuingDetailsEventHandler messageHandler) {
        DeferredResult<ServiceQueuingDetails> request = new DeferredResult<>(5000L, "Time Out");
        Long requestId = ThreadLocalRandom.current().nextLong();
        deferredResultHolder.save(requestId, request);

        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(messageHandler, "handleMessage");
        listenerAdapter.afterPropertiesSet();
        serviceQueuingEventResultListener.listenToChannel(Long.toString(requestId), listenerAdapter);

        ServiceQueuingDetails details = ServiceQueuingDetails.builder()
                .requestId(requestId)
                .method(method)
                .parameters(param)
                .build();
        serviceQueuingEventProducer.publish(clientUniqueKey, details);

        return request;
    }


}
