package com.example.bypass_server.queueService.subscriber.handler;

@FunctionalInterface
public interface ServiceQueuingDetailsEventHandler {
    void handleMessage(String requestId);
}
