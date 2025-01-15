package com.example.bypass_server.queueService.adaptor;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingDetailsEventHandler;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingAdaptor {
    DeferredResult<ServiceQueuingDetails> queueService(String clientUniqueKey, String method, Object param, ServiceQueuingDetailsEventHandler messageHandler);
}
