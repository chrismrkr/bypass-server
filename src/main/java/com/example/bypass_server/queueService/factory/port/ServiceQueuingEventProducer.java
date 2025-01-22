package com.example.bypass_server.queueService.factory.port;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;

public interface ServiceQueuingEventProducer {
    void publish(String clientUniqueId, ServiceQueuingDetails details);

}
