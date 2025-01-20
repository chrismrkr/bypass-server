package com.example.bypass_server.queueService.utils;

import com.example.bypass_server.queueService.domain.ServiceQueuingDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeferredServiceQueuingEventHolder {
    private final ConcurrentHashMap<Long, DeferredResult<Object>> deferredResultMap = new ConcurrentHashMap<>();

    public void save(Long requestId, DeferredResult<Object> deferredResult) {
        deferredResultMap.put(requestId, deferredResult);
    }

    public void delete(Long requestId) {
        deferredResultMap.remove(requestId);
    }

    public Optional<DeferredResult<Object>> get(Long requestId) {
        DeferredResult<Object> deferredResult = deferredResultMap.get(requestId);
        if(deferredResult == null) {
            return Optional.empty();
        } else {
            return Optional.of(deferredResult);
        }
    }
}
