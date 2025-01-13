package com.example.bypass_server.queueService.service.port;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface ServiceQueuingEventResultListener {
    void listenToChannel(String channelName, MessageListenerAdapter listenerAdapter);
}
