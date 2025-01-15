package com.example.bypass_server.queueService.adaptor.port;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;

public interface ServiceQueuingEventProducer {
    void publish(String clientUniqueId, ServiceQueuingDetails details);

}
