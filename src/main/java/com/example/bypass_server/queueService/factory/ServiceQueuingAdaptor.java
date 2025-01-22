package com.example.bypass_server.queueService.factory;

import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.factory.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;

public interface ServiceQueuingAdaptor {
    ServiceQueuingEventProducer getServiceQueuingEventProducer();
    DeferredServiceQueuingEventHolder getDeferredResultHolderWriter();
    ServiceQueuingEventResultListener getServiceQueuingEventResultListener();
}
