package com.example.bypass_server.queueService.factory.impl;

import com.example.bypass_server.queueService.factory.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
