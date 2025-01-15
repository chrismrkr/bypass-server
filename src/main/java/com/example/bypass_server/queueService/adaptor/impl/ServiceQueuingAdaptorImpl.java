package com.example.bypass_server.queueService.adaptor.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.adaptor.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.adaptor.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventHandler;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ServiceQueuingAdaptorImpl implements ServiceQueuingAdaptor {
    private final ServiceQueuingEventProducer serviceQueuingEventProducer;
    private final DeferredServiceQueuingEventHolder deferredResultHolder;
    private final ServiceQueuingEventResultListener serviceQueuingEventResultListener;

    @Override
    public ServiceQueuingEventProducer getServiceQueuingEventProducer() {
        return this.serviceQueuingEventProducer;
    }

    @Override
    public DeferredServiceQueuingEventHolder getDeferredResultHolderWriter() {
        return this.deferredResultHolder;
    }

    @Override
    public ServiceQueuingEventResultListener getServiceQueuingEventResultListener() {
        return this.serviceQueuingEventResultListener;
    }
}
