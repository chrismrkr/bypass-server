package com.example.bypass_server.queueService.manager.port;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;

public interface ServiceQueuingEventProducer {
    void publish(String topic, String partitioningKey, ServiceQueuingDetails details);

}
