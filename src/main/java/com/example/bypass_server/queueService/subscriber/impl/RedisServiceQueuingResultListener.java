package com.example.bypass_server.queueService.subscriber.impl;

import com.example.bypass_server.queueService.adaptor.port.ServiceQueuingEventResultListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisServiceQueuingResultListener implements ServiceQueuingEventResultListener {
    private final RedisMessageListenerContainer messageListenerContainer;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisServiceQueuingResultListener(RedisMessageListenerContainer messageListenerContainer) {
        this.messageListenerContainer = messageListenerContainer;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build(), ObjectMapper.DefaultTyping.NON_FINAL
        );
    }

    @Override
    public void listenToChannel(String channelName, MessageListenerAdapter listenerAdapter) {
        ChannelTopic topic = new ChannelTopic(channelName);
        listenerAdapter.setSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        messageListenerContainer.addMessageListener(listenerAdapter, topic);
    }
}
