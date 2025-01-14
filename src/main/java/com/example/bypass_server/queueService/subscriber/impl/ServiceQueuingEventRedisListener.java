package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.service.port.ServiceQueuingEventResultListener;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceQueuingEventRedisListener implements ServiceQueuingEventResultListener {
    private final RedisMessageListenerContainer messageListenerContainer;
    @Override
    public void listenToChannel(String channelName, MessageListenerAdapter listenerAdapter) {
        ChannelTopic topic = new ChannelTopic(channelName);
        messageListenerContainer.addMessageListener(listenerAdapter, topic);
        // MessageListenerAdaptor에 메세지를 받은 이후에 어떻게 처리할지를 정의하면 됨
        // 저기에 DeferredResult를 resolve하는 로직을 넣으면 될 듯함
    }

}
