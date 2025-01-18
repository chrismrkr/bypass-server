package com.example.bypass_server.bypassTest.service;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class BypassTestService {
    private final KafkaTemplate<String, ServiceQueuingDetails> serviceQueuingTopicProducer;

    @Autowired
    public BypassTestService(@Qualifier("serviceQueuingTopicProducer") KafkaTemplate<String, ServiceQueuingDetails> serviceQueuingTopicProducer) {
        this.serviceQueuingTopicProducer = serviceQueuingTopicProducer;
    }

    public DeferredResult<String> executeTask() {
        String topic = "topic";
        String key = "key";
        ServiceQueuingDetails build = ServiceQueuingDetails.builder()
                .requestId(1L)
                .target("aab")
                .method("aa")
                .parameters(new Object[]{1,2,3})
                .build();
        Object value = null;

        serviceQueuingTopicProducer.send(topic, key, build);
        return new DeferredResult<>(5000L, "TimeOut");
    }

    public String testMethod(String s) {
        return "[TEST RESULT] " + s;
    }

}
