package com.example.bypass_server.queueService.service.port;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;

public interface ServiceQueuingDetailsProducer {
    void publish(String clientUniqueId, ServiceQueuingDetails details);

}
