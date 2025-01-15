package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventHandler;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingManager {
    public DeferredResult<ServiceQueuingDetails> execute(String uniqueClientKey, String method, Object param, ServiceQueuingEventHandler handler);
}
