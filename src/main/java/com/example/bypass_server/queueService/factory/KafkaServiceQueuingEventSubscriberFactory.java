package com.example.bypass_server.queueService.factory;
import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.impl.KafkaServiceQueuingEventSubscriber;
import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;
import org.springframework.kafka.core.ConsumerFactory;

public class KafkaServiceQueuingEventSubscriberFactory<T> implements ServiceQueuingEventSubscriberFactory<T>  {
    private ApplicationServiceExecutor applicationServiceExecutor;
    private ServiceQueuingResultPublisher<T> serviceQueuingResultPublisher;
    private ConsumerFactory<String, ServiceQueuingDetails> consumerFactory;

    public KafkaServiceQueuingEventSubscriberFactory(ApplicationServiceExecutor applicationServiceExecutor, ServiceQueuingResultPublisher<T> serviceQueuingResultPublisher, ConsumerFactory<String, ServiceQueuingDetails> consumerFactory) {
        this.applicationServiceExecutor = applicationServiceExecutor;
        this.serviceQueuingResultPublisher = serviceQueuingResultPublisher;
        this.consumerFactory = consumerFactory;
    }

    @Override
    public KafkaServiceQueuingEventSubscriber<T> createInstance() {
        return new KafkaServiceQueuingEventSubscriber<T>(
                serviceQueuingResultPublisher,
                applicationServiceExecutor,
                consumerFactory
        );
    }
}
