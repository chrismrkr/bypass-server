package com.example.bypass_server.queueService.factory;


import com.example.bypass_server.queueService.subscriber.ServiceQueuingDetailsSubscriber;

public interface ServiceQueuingEventSubscriberFactory<T> {
    ServiceQueuingDetailsSubscriber createInstance();
}
