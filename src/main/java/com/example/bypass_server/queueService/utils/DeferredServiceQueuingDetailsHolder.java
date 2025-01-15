package com.example.bypass_server.queueService.utils;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import com.example.bypass_server.queueService.adaptor.port.DeferredResultHolderWriter;
import com.example.bypass_server.queueService.subscriber.port.DeferredResultHolderReader;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeferredServiceQueuingDetailsHolder implements DeferredResultHolderWriter<ServiceQueuingDetails>, DeferredResultHolderReader<ServiceQueuingDetails> {
    private final ConcurrentHashMap<Long, DeferredResult<ServiceQueuingDetails>> deferredResultMap = new ConcurrentHashMap<>();

    @Override
    public void save(Long requestId, DeferredResult<ServiceQueuingDetails> deferredResult) {
        deferredResultMap.put(requestId, deferredResult);
    }

    @Override
    public void delete(Long requestId) {
        deferredResultMap.remove(requestId);
    }
    @Override
    public Optional<DeferredResult<ServiceQueuingDetails>> get(Long requestId) {
        DeferredResult<ServiceQueuingDetails> deferredResult = deferredResultMap.get(requestId);
        if(deferredResult == null) {
            return Optional.empty();
        } else {
            return Optional.of(deferredResult);
        }
    }
}
