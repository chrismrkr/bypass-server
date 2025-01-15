package com.example.bypass_server.queueService.adaptor.port;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public interface ServiceQueuingEventResultListener {
    void listenToChannel(String channelName, MessageListenerAdapter listenerAdapter);
}
