package com.example.bypass_server.queueService.factory;
import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;
import com.example.bypass_server.queueService.subscriber.impl.KafkaServiceQueuingEventSubscriber;
import com.example.bypass_server.queueService.subscriber.port.ApplicationServiceExecutor;
import com.example.bypass_server.queueService.subscriber.port.ServiceQueuingResultPublisher;

public class KafkaServiceQueuingEventSubscriberFactory<T> implements ServiceQueuingEventSubscriberFactory<T>  {
    private ApplicationServiceExecutor applicationServiceExecutor;
    private ServiceQueuingResultPublisher<T> serviceQueuingResultPublisher;

    public KafkaServiceQueuingEventSubscriberFactory(ApplicationServiceExecutor applicationServiceExecutor, ServiceQueuingResultPublisher<T> serviceQueuingResultPublisher) {
        this.applicationServiceExecutor = applicationServiceExecutor;
        this.serviceQueuingResultPublisher = serviceQueuingResultPublisher;
    }

    @Override
    public KafkaServiceQueuingEventSubscriber<T> createInstance() {
        return new KafkaServiceQueuingEventSubscriber<T>(
                serviceQueuingResultPublisher,
                applicationServiceExecutor
        );
    }
}
