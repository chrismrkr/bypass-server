package com.example.bypass_server.queueService.adaptor;

import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;

public interface ServiceQueuingAdaptor {
    ServiceQueuingEventProducer getServiceQueuingEventProducer();
    DeferredServiceQueuingEventHolder getDeferredResultHolderWriter();
    ServiceQueuingEventResultListener getServiceQueuingEventResultListener();
}
