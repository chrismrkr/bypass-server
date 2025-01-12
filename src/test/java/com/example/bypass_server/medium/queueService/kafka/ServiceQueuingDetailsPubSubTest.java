package com.example.bypass_server.medium.queueService.kafka;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.service.port.ServiceQueuingDetailsProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceQueuingDetailsPubSubTest {
    @Autowired
    ServiceQueuingDetailsProducer serviceQueuingDetailsProducer;

    @Test
    void ServiceQueueDetails를_Kafka_파티션을_통해_PUB_SUB() throws InterruptedException {
        // given
        String clientUniqueId = "aabbbccdsaf1231gbfadsf123sfadfbfg5aaxsd-zd";
        ServiceQueuingDetails serviceQueuingDetails = ServiceQueuingDetails
                .builder()
                .requestId(1L)
                .method("/test")
                .parameters(null)
                .build();

        // when
        serviceQueuingDetailsProducer.publish(clientUniqueId, serviceQueuingDetails);
        Thread.sleep(2000L);
        // then

    }
}
