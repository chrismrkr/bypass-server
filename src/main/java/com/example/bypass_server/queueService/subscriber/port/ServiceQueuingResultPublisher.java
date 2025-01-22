package com.example.bypass_server.queueService.subscriber.port;

import com.example.bypass_server.queueService.subscriber.dto.QueuedServiceResult;

public interface ServiceQueuingResultPublisher<T> {
    void publishResult(String channel, QueuedServiceResult<T> msg);
}
