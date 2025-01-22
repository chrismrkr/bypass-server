package com.example.bypass_server.queueService.manager;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.handler.ServiceQueuingEventResultHandler;
import org.springframework.web.context.request.async.DeferredResult;

public interface ServiceQueuingManager<ResponseType> {
    DeferredResult<ResponseType> execute(ServiceQueuingEventResultHandler<ResponseType> handler, String partitionKey, Object target, String method, Object... param);

}
