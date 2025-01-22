package com.example.bypass_server.queueService.subscriber.handler;

import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;

@FunctionalInterface
public interface ServiceQueuingEventResultHandler<ResponseType> {
    void handleMessage(Long requestId, ResponseType response);
}
