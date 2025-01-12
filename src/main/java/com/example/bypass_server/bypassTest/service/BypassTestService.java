package com.example.bypass_server.bypassTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class BypassTestService {
    private final KafkaTemplate<String, Object> serviceQueuingTopicProducer;

    @Autowired
    public BypassTestService(@Qualifier("serviceQueuingTopicProducer") KafkaTemplate<String, Object> serviceQueuingTopicProducer) {
        this.serviceQueuingTopicProducer = serviceQueuingTopicProducer;
    }

    public DeferredResult<String> executeTask() {
        String topic = "topic";
        String key = "key";
        Object value = null;

        serviceQueuingTopicProducer.send(topic, key, value);
        return new DeferredResult<>(5000L, "TimeOut");
    }

}
