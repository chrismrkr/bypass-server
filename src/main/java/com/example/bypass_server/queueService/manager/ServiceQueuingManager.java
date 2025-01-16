package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventHandler;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingManager {
    DeferredResult<ServiceQueuingDetails> execute(ServiceQueuingEventHandler handler, String uniqueClientKey, Object target, String method, Object... param);
}
