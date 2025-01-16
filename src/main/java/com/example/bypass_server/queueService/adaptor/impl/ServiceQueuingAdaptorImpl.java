package com.example.bypass_server.queueService.adaptor.impl;

import com.example.bypass_server.queueService.adaptor.ServiceQueuingAdaptor;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventProducer;
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
