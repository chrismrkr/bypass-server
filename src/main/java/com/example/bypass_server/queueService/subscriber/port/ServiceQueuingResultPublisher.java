package com.example.bypass_server.queueService.subscriber.port;

public interface ServiceQueuingResultPublisher {
    void publishResult(String channel, Object message);
}
