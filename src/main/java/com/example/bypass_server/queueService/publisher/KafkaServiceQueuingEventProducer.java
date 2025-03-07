package com.example.bypass_server.queueService.publisher;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.manager.port.ServiceQueuingEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class KafkaServiceQueuingEventProducer implements ServiceQueuingEventProducer {
    private final KafkaTemplate<String, ServiceQueuingDetails> kafkaTemplate;
    private final Environment env;
    @Autowired
    public KafkaServiceQueuingEventProducer(@Qualifier("serviceQueuingTopicProducer") KafkaTemplate<String, ServiceQueuingDetails> kafkaTemplate,
                                            Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.env = environment;
    }

    @Override
    public void publish(String topic, String partitioningKey, ServiceQueuingDetails details) {
        log.info("[Queued Event Publish] {} {}", details.getTarget(), details.getMethod());
        kafkaTemplate.send(Objects.requireNonNull(topic), partitioningKey, details);
    }
}
