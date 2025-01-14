package com.example.bypass_server.queueService.service.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.service.ServiceQueuingDetailsService;
import com.example.bypass_server.queueService.service.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.service.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.service.port.ServiceQueuingDetailsProducer;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingDetailsEventHandler;
import jakarta.websocket.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ServiceQueuingDetailsServiceImpl implements ServiceQueuingDetailsService {
    private final ServiceQueuingDetailsProducer serviceQueuingDetailsProducer;
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
        serviceQueuingDetailsProducer.publish(clientUniqueKey, details);

        return request;
    }


}
