package com.example.bypass_server.queueService.subscriber.handler;

@FunctionalInterface
public interface ServiceQueuingEventHandler {
    void handleMessage(String requestId);
}
