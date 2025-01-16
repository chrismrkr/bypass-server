package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingManager {
    DeferredResult<ServiceQueuingDetails> execute(ServiceQueuingEventResultHandler handler, String uniqueClientKey, Object target, String method, Object... param);
    DeferredResult<ServiceQueuingDetails> execute(ServiceQueuingEventResultHandler handler, String uniqueClientKey, String targetBeanName, String method, Object... param);
}
