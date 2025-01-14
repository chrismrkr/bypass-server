package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.bypassTest.handler.RedisMessageHandler;
import com.example.bypass_server.queueService.service.port.ServiceQueuingEventResultListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceQueuingEventRedisListener implements ServiceQueuingEventResultListener {
    private final RedisMessageListenerContainer messageListenerContainer;
    @Override
    public void listenToChannel(String channelName, MessageListenerAdapter listenerAdapter) {
        ChannelTopic topic = new ChannelTopic(channelName);
        messageListenerContainer.addMessageListener(listenerAdapter, topic);
    }

}
