package com.example.bypass_server.queueService.subscriber.handler;

@FunctionalInterface
public interface ServiceQueuingEventResultHandler {
    void handleMessage(String requestId);
}
