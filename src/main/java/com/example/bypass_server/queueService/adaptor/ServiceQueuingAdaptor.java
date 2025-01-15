package com.example.bypass_server.queueService.adaptor;

import com.example.bypass_server.queueService.adaptor.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventProducer;
import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventHandler;
import com.example.bypass_server.queueService.utils.DeferredServiceQueuingEventHolder;
import lombok.Getter;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingAdaptor {
    ServiceQueuingEventProducer getServiceQueuingEventProducer();
    DeferredServiceQueuingEventHolder getDeferredResultHolderWriter();
    ServiceQueuingEventResultListener getServiceQueuingEventResultListener();
}
