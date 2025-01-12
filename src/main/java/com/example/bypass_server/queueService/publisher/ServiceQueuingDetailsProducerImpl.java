package com.example.bypass_server.queueService.publisher;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.service.port.ServiceQueuingDetailsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class ServiceQueuingDetailsProducerImpl implements ServiceQueuingDetailsProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Environment env;
    @Autowired
    public ServiceQueuingDetailsProducerImpl(@Qualifier("serviceQueuingTopicProducer") KafkaTemplate<String, Object> kafkaTemplate,
                                             Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.env = environment;
    }

    @Override
    public void publish(String clientUniqueId, ServiceQueuingDetails details) {
        String topic = env.getProperty("spring.kafka.topic.service-queuing.topic-name");
        kafkaTemplate.send(Objects.requireNonNull(topic), clientUniqueId, details);
        log.info("[Kafka PUBLISH] {} produced", clientUniqueId);
    }
}
