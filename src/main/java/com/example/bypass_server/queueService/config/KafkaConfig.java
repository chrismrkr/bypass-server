package com.example.bypass_server.queueService.config;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final Environment env;

    @Bean(name = "serviceQueuingTopicProducer")
    public KafkaTemplate<String, Object> serviceQueuingTopicProducer() {
        return new KafkaTemplate<>(serviceQueuingTopicProducerFactory());
    }
    @Bean
    public ProducerFactory<String, Object> serviceQueuingTopicProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16KB 배치 크기
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "serviceQueuingTopicConsumerConfig")
    public ConsumerBriefConfig serviceQueuingTopicConsumerConfig() {
        ConsumerBriefConfig kafkaConsumerConfigUtils = new ConsumerBriefConfig();
        kafkaConsumerConfigUtils.setTopicName(env.getProperty("spring.kafka.topic.service-queuing.topic-name"));
        kafkaConsumerConfigUtils.setGroupId(env.getProperty("spring.kafka.topic.service-queuing.group-id"));
        return kafkaConsumerConfigUtils;
    }

    @Bean(name = "serviceQueuingTopicKafkaConsumerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ServiceQueuingDetails> serviceQueuingTopicKafkaConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ServiceQueuingDetails> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(serviceQueuingTopicConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, ServiceQueuingDetails> serviceQueuingTopicConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // manual commit
        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                jsonDeserializer()
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build(), ObjectMapper.DefaultTyping.NON_FINAL
        );
        return objectMapper;
    }

    @Bean
    public JsonDeserializer<ServiceQueuingDetails> jsonDeserializer() {
        JsonDeserializer<ServiceQueuingDetails> serviceQueuingDetailsJsonDeserializer = new JsonDeserializer<>(ServiceQueuingDetails.class);
        return serviceQueuingDetailsJsonDeserializer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConsumerBriefConfig {
        private String topicName;
        private String groupId;
    }
}
