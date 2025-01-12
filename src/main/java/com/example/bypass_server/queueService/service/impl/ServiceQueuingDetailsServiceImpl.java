package com.example.bypass_server.queueService.service.impl;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.service.ServiceQueuingDetailsService;
import com.example.bypass_server.queueService.service.port.DeferredResultEventListener;
import com.example.bypass_server.queueService.service.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.service.port.ServiceQueuingDetailsProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ServiceQueuingDetailsServiceImpl implements ServiceQueuingDetailsService {
    private final ServiceQueuingDetailsProducer serviceQueuingDetailsProducer;
    private final DeferredResultHolderWriter<ServiceQueuingDetails> deferredResultHolder;
    private final DeferredResultEventListener deferredResultEventListener;

    @Override
    public DeferredResult<ServiceQueuingDetails> queueService(String clientUniqueKey, String method) {
        DeferredResult<ServiceQueuingDetails> request = new DeferredResult<>(5000L, "Time Out");
        Long requestId = ThreadLocalRandom.current().nextLong();
        ServiceQueuingDetails details = ServiceQueuingDetails.builder()
                .requestId(requestId)
                .method(method)
                .parameters(null)
                .build();

        // 1. add queue
        serviceQueuingDetailsProducer.publish(clientUniqueKey, details);

        // 2. save deferredResult
        deferredResultHolder.save(requestId, request);

        // 3. subscribe DeferredResultEvent


        return request;
    }
}
